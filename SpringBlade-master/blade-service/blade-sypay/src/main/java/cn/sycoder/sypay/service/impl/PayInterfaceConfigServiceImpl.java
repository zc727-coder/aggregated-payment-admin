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
package cn.sycoder.sypay.service.impl;


import cn.sycoder.sypay.dto.PayInfoDto;
import cn.sycoder.sypay.dto.PayInterfaceDefineDto;
import cn.sycoder.sypay.entity.*;
import cn.sycoder.sypay.mapper.PayInterfaceConfigMapper;
import cn.sycoder.sypay.mapper.PayInterfaceDefineMapper;
import cn.sycoder.sypay.service.IPayInterfaceConfigService;
import cn.sycoder.sypay.service.IPayInterfaceDefineService;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class PayInterfaceConfigServiceImpl extends BaseServiceImpl<PayInterfaceConfigMapper, PayInterfaceConfig> implements IPayInterfaceConfigService {

	private MerchantAppServiceImpl service;

	private PayPassageServiceImpl payPassageService;

	private PayWayServiceImpl payWayService;

	@Override
	@Transactional
	public boolean payOnSubmit(PayInfoDto dto) {
		//检验应用是否存在
		MerchantApp app = service.getOne(MerchantApp.gw()
			.eq(MerchantApp::getAppId, dto.getInfoId()));
		if(app == null || app.getStatus() != 1){
			throw  new ServiceException("应用不存在");
		}
		PayInterfaceConfig paysConfig = new PayInterfaceConfig();
		paysConfig.setInfoId(dto.getInfoId());
		paysConfig.setStatus(dto.getStatus());
		paysConfig.setIfCode(dto.getIfCode());
		paysConfig.setRemark(dto.getRemark());
		paysConfig.setIfParams(JSONObject.toJSONString(dto.getIfParams()));
		paysConfig.setInfoType(3);
		//处理费率
		if(StringUtil.isNotBlank(dto.getIfParams().getUseCert()) ){
			paysConfig.setIfRate(new BigDecimal(dto.getIfParams().getUseCert())
				.divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP));
		}
		//查询当前商户的商户参数配置
		PayInterfaceConfig dbRecord = this.getOne(PayInterfaceConfig.gw()
			.eq(PayInterfaceConfig::getInfoType, 3)
			.eq(PayInterfaceConfig::getInfoId, dto.getInfoId())
			.eq(PayInterfaceConfig::getIfCode, dto.getIfCode())//前端配置的是什么支付方式就查什么支付方式
		);
		//第一次创建//第二次更新
		if(dbRecord !=null){
			paysConfig.setId(dbRecord.getId());
			//更新的情况下需要合并支付参数
		}
		boolean result = this.saveOrUpdate(paysConfig);
		//存入MQ

		//存入支付渠道
			//先删除后存储
		payPassageService.remove(PayPassage.gw()
			.eq(PayPassage::getMchNo,dto.getMerchantNo())
			.eq(PayPassage::getAppId,dto.getInfoId())
			.eq(PayPassage::getIfCode,dto.getIfCode())
		);
//		从支付方式获取支付渠道
		List<PayWay> list = payWayService.list(PayWay.gw()
			.eq(PayWay::getIfCode, dto.getIfCode())
		);
		if(list != null){
			List<PayPassage> listPassage = new ArrayList<>();
			for (PayWay payWay : list) {
				PayPassage passage = new PayPassage();
				listPassage.add(passage);

				passage.setAppId(dto.getInfoId());
				passage.setMchNo(dto.getMerchantNo());
				passage.setIfCode(dto.getIfCode());
				passage.setRate(new BigDecimal("0.01"));
				passage.setWayCode(payWay.getWayCode());
				passage.setStatus(1);
				passage.setIsDeleted(0);
			}
			//存储支付渠道
			payPassageService.saveBatch(listPassage);
		}


		return result;
	}
}
