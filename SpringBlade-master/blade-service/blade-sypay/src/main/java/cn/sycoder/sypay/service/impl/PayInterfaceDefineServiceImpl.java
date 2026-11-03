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


import cn.sycoder.sypay.dto.PayInterfaceDefineDto;
import cn.sycoder.sypay.entity.PayInterfaceDefine;
import cn.sycoder.sypay.entity.PayWay;
import cn.sycoder.sypay.mapper.PayInterfaceDefineMapper;
import cn.sycoder.sypay.mapper.PayWayMapper;
import cn.sycoder.sypay.service.IPayInterfaceDefineService;
import cn.sycoder.sypay.service.IPayWayService;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class PayInterfaceDefineServiceImpl extends BaseServiceImpl<PayInterfaceDefineMapper, PayInterfaceDefine> implements IPayInterfaceDefineService {


	@Override
	public boolean submit(PayInterfaceDefineDto dto) {
		String s = JSONObject.toJSONString(dto.getCheckedCodes());
		dto.setWayCodes(s);
		boolean ret = this.saveOrUpdate(dto);
		return ret;
	}
}
