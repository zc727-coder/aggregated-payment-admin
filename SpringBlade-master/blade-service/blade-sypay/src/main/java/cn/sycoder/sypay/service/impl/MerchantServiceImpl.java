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


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.sycoder.sypay.entity.Merchant;
import cn.sycoder.sypay.entity.MerchantApp;
import cn.sycoder.sypay.mapper.MerchantMapper;
import cn.sycoder.sypay.service.IMerchantAppService;
import cn.sycoder.sypay.service.IMerchantService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.constant.SyPayConstant;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.cloud.commons.util.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class MerchantServiceImpl extends BaseServiceImpl<MerchantMapper, Merchant> implements IMerchantService {

	private IMerchantAppService merchantAppService;

	@Override
	@Transactional
	public boolean submit(Merchant dto) {
		dto.setMerchantNo("M" + DateUtil.currentSeconds());
		boolean ret = saveOrUpdate(dto);
//		dto.setIsDeleted(SyPayConstant.NOT_DELETED);
		if (!ret) {
			throw new ServiceException("新增商户失败!");
		}
		//创建默认应用的逻辑
		MerchantApp app = new MerchantApp();
		app.setAppId(IdUtil.objectId());//工具类待定
		app.setMerchantNo(dto.getMerchantNo());
		app.setAppName(dto.getMerchantName() + "默认应用");
		app.setAppSecret(RandomUtil.randomString(128));//待定
		app.setStatus(SyPayConstant.ENABLE);
		boolean appRet = merchantAppService.save(app);
		if (!appRet) {
			throw new ServiceException("新增商户应用失败!");
		}
		return true;
	}

	@Override
	@Transactional
	/**
	 * 开发中逻辑先后的问题
	 * 面试官：说说你在开发中遇到的问题，并且说一下怎么解决的
	 */
	public boolean deleteById(List<Long> ids) {
		List<Merchant> list = this.listByIds(ids);
		boolean ret = this.deleteLogic(ids);//删除商户
		if(!ret){
			throw new ServiceException("删除商户失败!");
		}
		for (Merchant merchant : list) {
			QueryWrapper<MerchantApp> queryWapper = new QueryWrapper<>();
			queryWapper.lambda().eq(MerchantApp::getMerchantNo,merchant.getMerchantNo());
			boolean remove = merchantAppService.remove(queryWapper);
			if(!remove){
				throw new ServiceException("删除商户应用失败!");
			}
		}

		return true;
	}
}
