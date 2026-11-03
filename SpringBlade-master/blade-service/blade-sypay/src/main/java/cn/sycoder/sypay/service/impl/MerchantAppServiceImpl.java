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


import cn.sycoder.sypay.dto.MerchantAppDto;
import cn.sycoder.sypay.entity.Merchant;
import cn.sycoder.sypay.entity.MerchantApp;
import cn.sycoder.sypay.mapper.MerchantAppMapper;
import cn.sycoder.sypay.mapper.MerchantMapper;
import cn.sycoder.sypay.service.IMerchantAppService;
import cn.sycoder.sypay.service.IMerchantService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.AllArgsConstructor;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class MerchantAppServiceImpl extends BaseServiceImpl<MerchantAppMapper, MerchantApp> implements IMerchantAppService {

	@Override
	public IPage<MerchantApp> listPages(MerchantServiceImpl merchantService
		, MerchantAppDto dto, Query query) {
		BladeUser user = SecureUtil.getUser();
		if (!"000000".equals(user.getTenantId())) {
			Merchant merchant = merchantService.getOne(Merchant.gw()
				.eq(Merchant::getAccount, user.getAccount())
				.eq(Merchant::getTelnet, user.getTenantId())
			);
			if (merchant != null) {
				dto.setMerchantNo(merchant.getMerchantNo());
			}
		}
		return this.page(Condition.getPage(query), Condition.getQueryWrapper(dto));
	}
}
