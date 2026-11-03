/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sycoder.sypay.payorder.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.sycoder.contants.CS;
import cn.sycoder.sypay.entity.Merchant;
import cn.sycoder.sypay.entity.MerchantApp;
import cn.sycoder.sypay.entity.PayPassage;
import cn.sycoder.sypay.model.MchAppConfigContext;
import cn.sycoder.sypay.model.config.SpringBeansUtil;
import cn.sycoder.sypay.model.params.NormalMchParams;
import cn.sycoder.sypay.payorder.channel.IPaymentService;
import cn.sycoder.sypay.payorder.dto.UnifiedOrder;
import cn.sycoder.sypay.payorder.entity.PayOrder;
import cn.sycoder.sypay.payorder.mapper.PayOrderMapper;
import cn.sycoder.sypay.payorder.rqrs.AbstractRS;
import cn.sycoder.sypay.payorder.rqrs.msg.ChannelRetMsg;
import cn.sycoder.sypay.payorder.rqrs.payorder.UnifiedOrderRQ;
import cn.sycoder.sypay.payorder.rqrs.payorder.UnifiedOrderRS;
import cn.sycoder.sypay.payorder.service.IPayOrderService;
import cn.sycoder.sypay.payorder.utils.SeqKit;
import cn.sycoder.sypay.service.IPayPassageService;
import cn.sycoder.sypay.tools.AmountUtil;
import cn.sycoder.sypay.tools.SypayKit;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class PayOrderServiceImpl extends BaseServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

	@Autowired
	IPayPassageService payPassageService;

	@Autowired
	ConfigContextQueryService configContextQueryService;

	@Override
	public R pay(UnifiedOrderRQ rq) {
//		1)根据前端传值，判断当前支付具体渠道是啥，用SypayKit工具类中的getPayWayCodeByBarCode()方法获取支付渠道；验证当前支付渠道是否支持
		UnifiedOrderRQ bizRQ = buildBizRQ(rq);
//		2)判断当前订单号是否存在，存在报错
		return unifiedOrder(bizRQ);
//		3)获取支付参数 (缓存数据) 和商户信息
//		4)根据支付方式，查询出该商户可用的支付接口
//		5)获取支付接口
//		6)生成支付订单
//		7)调起支付接口（payway 里面有）
//		8)处理返回的渠道信息，并更新订单状态
//		9)统一封装订单数据返回
	}

	/**
	 * 统一下单
	 * 1：如果存在则返回订单已经存在
	 * 2：如果不存在则创建新订单
	 *
	 * @param bizRQ
	 */
	private R unifiedOrder(UnifiedOrderRQ bizRQ) {
		UnifiedOrderRS rs = null;
		//判断是否存在，存在则结束
		if (this.getOne(PayOrder.gw()
			.eq(PayOrder::getMchOrderNo, bizRQ.getMchOrderNo())
			.eq(PayOrder::getMchNo, bizRQ.getMchNo())
			.eq(PayOrder::getAppId, bizRQ.getAppId())
		) != null) {
			throw new ServiceException("商户订单: " + bizRQ.getMchOrderNo() + "已经存在");
		}
		//订单号不存在，创建新订单
		//获取支付参数（缓存信息） 和 商户信息
		MchAppConfigContext mchAppConfigContext = configContextQueryService.queryMchInfoAndAppInfo(bizRQ.getMchNo(), bizRQ.getAppId());
		if (mchAppConfigContext == null) {
			throw new ServiceException("商户与应用信息不存在");
		}
		//获取当前商户信息和应用信息
		Merchant mchInfo = mchAppConfigContext.getMchInfo();
		MerchantApp mchApp = mchAppConfigContext.getMchApp();
//		4)根据支付方式，查询出该商户可用的支付接口
		PayPassage mchPayPassage = payPassageService.findMchPayPassage(bizRQ.getMchNo(), bizRQ.getAppId(), bizRQ.getWayCode());
		if (mchPayPassage == null) {
			throw new ServiceException("商户支付渠道不存在");
		}
		//获取支付接口
		IPaymentService paymentService = checkMchWayCodeAndGetService(mchAppConfigContext, mchPayPassage);
		//生成订单操作
		PayOrder payOrder = genPayOder(bizRQ, mchInfo, mchApp, mchPayPassage);
		this.save(payOrder);
		//支付操作
		try {
			//调用支付接口
			rs = (UnifiedOrderRS) paymentService.pay(bizRQ, payOrder, mchAppConfigContext);
			this.processChannelMsg(rs.getChannelRetMsg(), payOrder);
			//处理支付后的逻辑
		} catch (Exception e) {
			e.printStackTrace();
		}
		//统一响应处理
		return rsPayOrder(bizRQ, payOrder, rs);
		//支付成功，通知商家
		// mq 发送一个消息
		//商家系统，接收到生产者消息，产生消息的消费

		//刷刷力扣
		//背八股文
		//准备面试了

	}

	private R rsPayOrder(UnifiedOrderRQ bizRQ, PayOrder payOrder, UnifiedOrderRS rs) {

		rs.setPayOrderId(payOrder.getPayOrderId());
		rs.setOrderState(payOrder.getStatus());
		rs.setMchOrderNo(payOrder.getMchOrderNo());
		if (payOrder.getStatus() == PayOrder.STATE_FAIL) {
			rs.setErrCode(rs.getChannelRetMsg() != null ? rs.getErrCode() : null);
			rs.setErrMsg(rs.getChannelRetMsg() != null ? rs.getChannelRetMsg().getChannelErrMsg() : null);
		}
		return R.data(rs);
	}

	private void processChannelMsg(ChannelRetMsg channelRetMsg, PayOrder payOrder) {
		if (channelRetMsg == null) {
			return;
		}
		//支付成功的处理
		if (ChannelRetMsg.ChannelState.CONFIRM_SUCCESS == channelRetMsg.getChannelState()) {
			this.updateInitOrder(PayOrder.STATE_SUCCESS, payOrder, channelRetMsg);
			//处理其它的支付逻辑
//				分账处理
//				报表
		} else if (ChannelRetMsg.ChannelState.CONFIRM_FAIL == channelRetMsg.getChannelState()) {
			//支付失败
			this.updateInitOrder(PayOrder.STATE_FAIL, payOrder, channelRetMsg);
		} else if (ChannelRetMsg.ChannelState.WAITING == channelRetMsg.getChannelState()
			|| ChannelRetMsg.ChannelState.UNKNOWN == channelRetMsg.getChannelState()
			|| ChannelRetMsg.ChannelState.API_RET_ERROR == channelRetMsg.getChannelState()
		) {
			//处理中，处理失败，不知道的情况
			this.updateInitOrder(PayOrder.STATE_ING, payOrder, channelRetMsg);
		} else {
			throw new ServiceException("支付状态异常");
		}
	}

	private void updateInitOrder(int stateSuccess, PayOrder payOrder, ChannelRetMsg channelRetMsg) {
		payOrder.setStatus(stateSuccess);
		payOrder.setChannelOrderNo(channelRetMsg.getChannelOrderId());
		payOrder.setErrCode(channelRetMsg.getChannelErrCode());
		payOrder.setErrMsg(channelRetMsg.getChannelErrMsg());
		boolean ret = this.updateOrder2successOrFail(payOrder, channelRetMsg);
		if (!ret) {
			throw new ServiceException("更新订单异常");
		}

	}

	private boolean updateOrder2successOrFail(PayOrder payOrder, ChannelRetMsg channelRetMsg) {
		if (payOrder.getStatus() == PayOrder.STATE_SUCCESS) {
			return updateOrder2Success(payOrder, channelRetMsg);
		} else if (payOrder.getStatus() == PayOrder.STATE_FAIL) {
			return updateOrder2Fail(payOrder, channelRetMsg);
		}
		return false;

	}

	private boolean updateOrder2Fail(PayOrder payOrder, ChannelRetMsg channelRetMsg) {
		PayOrder failOrder = new PayOrder();
		failOrder.setStatus(PayOrder.STATE_FAIL);
		failOrder.setChannelOrderNo(channelRetMsg.getChannelOrderId());
		failOrder.setErrMsg(channelRetMsg.getChannelErrMsg());
		failOrder.setErrCode(channelRetMsg.getChannelErrCode());
		return update(failOrder, new LambdaUpdateWrapper<PayOrder>()
			.eq(PayOrder::getPayOrderId, payOrder.getPayOrderId())
			.eq(PayOrder::getStatus, PayOrder.STATE_INIT)
		);
	}

	private boolean updateOrder2Success(PayOrder payOrder, ChannelRetMsg channelRetMsg) {
		PayOrder successOrder = new PayOrder();
		successOrder.setStatus(PayOrder.STATE_SUCCESS);
		successOrder.setChannelOrderNo(channelRetMsg.getChannelOrderId());
		successOrder.setSuccessTime(new Date());
		return update(successOrder, new LambdaUpdateWrapper<PayOrder>()
			.eq(PayOrder::getPayOrderId, payOrder.getPayOrderId())
			.eq(PayOrder::getStatus, PayOrder.STATE_INIT)
		);
	}

	private PayOrder genPayOder(UnifiedOrderRQ rq, Merchant mchInfo
		, MerchantApp mchApp, PayPassage mchPayPassage) {
		PayOrder payOrder = new PayOrder();
		payOrder.setSubject(rq.getSubject());
		payOrder.setPayOrderId(SeqKit.genPayOrderId());
		payOrder.setMchNo(mchInfo.getMerchantNo());
		payOrder.setIsvNo(mchInfo.getServerNo());
		payOrder.setMchName(mchInfo.getMerchantName());
		payOrder.setMchType(mchInfo.getType());
		payOrder.setAmount(rq.getAmount());
		payOrder.setMchOrderNo(rq.getMchOrderNo());
		payOrder.setIfCode(mchPayPassage.getIfCode());
		payOrder.setAppId(mchApp.getAppId());
		payOrder.setWayCode(rq.getWayCode());
		payOrder.setBody(rq.getBody());

		//预下单 提前给个状态
		payOrder.setMchFeeRate(BigDecimal.ZERO);
		payOrder.setMchFeeAmount(
			AmountUtil.calPercentageFee(payOrder.getAmount(), BigDecimal.ZERO));
		payOrder.setCurrency(rq.getCurrency());
		payOrder.setStatus(PayOrder.STATE_INIT);
		Date date = new Date();
		//设置订单过期时间
		if (rq.getExpiredTime() != null) {
			payOrder.setExpiredTime(DateUtil.offsetSecond(date, rq.getExpiredTime()));
		} else {
			//订单过期时间，30分钟过期
			payOrder.setExpiredTime(DateUtil.offsetMinute(date, 30));
		}
		payOrder.setCreateTime(date);
		return payOrder;
	}

	/**
	 * 获取支付接口
	 *
	 * @param mchAppConfigContext
	 * @param mchPayPassage
	 * @return
	 */
	private IPaymentService checkMchWayCodeAndGetService(
		MchAppConfigContext mchAppConfigContext, PayPassage mchPayPassage) {
		//通过ifcode + waycode 获取具体的支付接口
		String ifCode = mchPayPassage.getIfCode();
		IPaymentService paymentService = SpringBeansUtil.getBean(ifCode + "PaymentService", IPaymentService.class);
		if (paymentService == null) {
			throw new ServiceException("没有这个支付渠道");
		}
		if (!paymentService.isSupport(mchPayPassage.getWayCode())) {
			throw new ServiceException("该接口不支持该支付方式");
		}
		NormalMchParams mchParams = configContextQueryService.queryNormalMchParams(mchAppConfigContext.getMchNo()
			, mchAppConfigContext.getAppId(), mchPayPassage.getIfCode());
		if (mchParams == null) {
			throw new ServiceException("商户应用参数未配置");
		}
		return paymentService;
	}

	private UnifiedOrderRQ buildBizRQ(UnifiedOrderRQ rq) {
		//获取前端传过来的支付方式
		String wayCode = rq.getWayCode();
		//判断是否是自己公司收银台的支付
		if (CS.PAY_WAY_CODE.QR_CASHIER.equals(wayCode)) {
			//处理收银台支付逻辑
		}
		//如果是自动识别条码
		if (CS.PAY_WAY_CODE.AUTO_BAR.equals(wayCode)) {
			//判断是什么类型的支付(如果前端选择的不是具体类型的支付方式，那么获取到的还是自动支付)
			UnifiedOrderRQ bizRQ = rq.buildBizRQ();
			//需要通过条形码信息获取到具体支付的类型
			wayCode = SypayKit.getPayWayCodeByBarCode(bizRQ.getAuthCode());
			rq.setWayCode(wayCode);
		}
		//判断当前的wayCode 是否已经配置支持
		if (payPassageService.count(PayPassage.gw().eq(PayPassage::getWayCode, wayCode)) <= 0) {
			throw new ServiceException("当前支付渠道不支持");
		}
		return rq.buildBizRQ();
	}
}
