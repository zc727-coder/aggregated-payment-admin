/*
 * Copyright (c) 2021-2031, 河北计全科技有限公司 (https://www.jeequan.com & jeequan@126.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sycoder.sypay.payorder.channel.alipay.payway;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.sycoder.sypay.model.MchAppConfigContext;
import cn.sycoder.sypay.payorder.channel.alipay.AlipayKit;
import cn.sycoder.sypay.payorder.channel.alipay.AlipayPaymentService;
import cn.sycoder.sypay.payorder.entity.PayOrder;
import cn.sycoder.sypay.payorder.rqrs.AbstractRS;
import cn.sycoder.sypay.payorder.rqrs.msg.ChannelRetMsg;
import cn.sycoder.sypay.payorder.rqrs.payorder.UnifiedOrderRQ;
import cn.sycoder.sypay.payorder.rqrs.payorder.payway.alipay.AliJsapiOrderRQ;
import cn.sycoder.sypay.payorder.rqrs.payorder.payway.alipay.AliJsapiOrderRS;
import cn.sycoder.sypay.payorder.utils.ApiResBuilder;
import cn.sycoder.sypay.tools.AmountUtil;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.stereotype.Service;

/*
 * 支付宝 jsapi支付
 */
@Service("alipayPaymentByJsapiService") //Service Name需保持全局唯一性
public class AliJsapi extends AlipayPaymentService {

    @Override
    public String preCheck(UnifiedOrderRQ rq, PayOrder payOrder) {

        AliJsapiOrderRQ bizRQ = (AliJsapiOrderRQ) rq;
        if(StringUtils.isEmpty(bizRQ.getBuyerUserId())){
            throw new ServiceException("[buyerUserId]不可为空");
        }

        return null;
    }

    @Override
    public AbstractRS pay(UnifiedOrderRQ rq, PayOrder payOrder, MchAppConfigContext mchAppConfigContext) throws Exception{

        AliJsapiOrderRQ bizRQ = (AliJsapiOrderRQ) rq;

        AlipayTradeCreateRequest req = new AlipayTradeCreateRequest();
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(payOrder.getPayOrderId());
        model.setSubject(payOrder.getSubject()); //订单标题
        model.setBody(payOrder.getBody()); //订单描述信息
        model.setTotalAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));  //支付金额
        model.setTimeExpire(DateUtil.format(payOrder.getExpiredTime(), DatePattern.NORM_DATETIME_FORMAT));  // 订单超时时间
        model.setBuyerId(bizRQ.getBuyerUserId());
        req.setNotifyUrl(getNotifyUrl()); // 设置异步通知地址
        req.setBizModel(model);

        //统一放置 isv接口必传信息
        AlipayKit.putApiIsvInfo(mchAppConfigContext, req, model);

        //调起支付宝 （如果异常， 将直接跑出   ChannelException ）
        AlipayTradeCreateResponse alipayResp = configContextQueryService.getAlipayClientWrapper(mchAppConfigContext).execute(req);

        // 构造函数响应数据
        AliJsapiOrderRS res = ApiResBuilder.buildSuccess(AliJsapiOrderRS.class);
        ChannelRetMsg channelRetMsg = new ChannelRetMsg();
        res.setChannelRetMsg(channelRetMsg);

        //放置 响应数据
        channelRetMsg.setChannelAttach(alipayResp.getBody());

        // ↓↓↓↓↓↓ 调起接口成功后业务判断务必谨慎！！ 避免因代码编写bug，导致不能正确返回订单状态信息  ↓↓↓↓↓↓
        res.setAlipayTradeNo(alipayResp.getTradeNo());

        channelRetMsg.setChannelOrderId(alipayResp.getTradeNo());
        if(alipayResp.isSuccess()){ //业务处理成功
            channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.WAITING);

        }else{
            channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_FAIL);
            channelRetMsg.setChannelErrCode(AlipayKit.appendErrCode(alipayResp.getCode(), alipayResp.getSubCode()));
            channelRetMsg.setChannelErrMsg(AlipayKit.appendErrMsg(alipayResp.getMsg(), alipayResp.getSubMsg()));
        }
        return res;
    }

}
