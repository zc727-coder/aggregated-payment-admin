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


import cn.sycoder.contants.CS;
import cn.sycoder.sypay.entity.PayInterfaceDefine;
import cn.sycoder.sypay.entity.PayPassage;
import cn.sycoder.sypay.entity.PayWay;
import cn.sycoder.sypay.mapper.PayPassageMapper;
import cn.sycoder.sypay.mapper.PayWayMapper;
import cn.sycoder.sypay.service.IPayPassageService;
import cn.sycoder.sypay.service.IPayWayService;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class PayPassageServiceImpl extends BaseServiceImpl<PayPassageMapper, PayPassage> implements IPayPassageService {

	@Autowired
	PayInterfaceDefineServiceImpl payDefineConfigService;

	public PayPassage findMchPayPassage(String mchNo, String appId, String wayCode) {
		List<PayPassage> list = list(PayPassage.gw()
			.eq(PayPassage::getMchNo, mchNo)
			.eq(PayPassage::getAppId, appId)
			.eq(PayPassage::getStatus, CS.YES)
			.eq(PayPassage::getWayCode, wayCode)
		);

		if (list.isEmpty()) {
			return null;
		} else { // 返回一个可用通道

			HashMap<String, PayPassage> mchPayPassageMap = new HashMap<>();

			for (PayPassage mchPayPassage : list) {
				mchPayPassageMap.put(mchPayPassage.getIfCode(), mchPayPassage);
			}
			// 查询ifCode所有接口
			PayInterfaceDefine interfaceDefine = payDefineConfigService
				.getOne(PayInterfaceDefine.gw()
					.select(PayInterfaceDefine::getIfCode, PayInterfaceDefine::getStatus)
					.eq(PayInterfaceDefine::getStatus, CS.YES)
					.in(PayInterfaceDefine::getIfCode, mchPayPassageMap.keySet()), false);

			if (interfaceDefine != null) {
				return mchPayPassageMap.get(interfaceDefine.getIfCode());
			}
		}
		return null;
	}
}
