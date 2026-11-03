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
package cn.sycoder.sypay.controller;

import cn.sycoder.sypay.dto.IfParams;
import cn.sycoder.sypay.dto.PayInfoDto;
import cn.sycoder.sypay.dto.PayInterfaceConfigDto;
import cn.sycoder.sypay.entity.PayInterfaceConfig;
import cn.sycoder.sypay.model.params.NormalMchParams;
import cn.sycoder.sypay.service.IPayInterfaceConfigService;
import cn.sycoder.sypay.service.IPayInterfaceConfigService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.common.cache.CacheNames;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 岗位表 控制器
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@RequestMapping("/pay/configs")
@Api(value = "岗位表", tags = "岗位表接口")
public class PayInterfaceConfigController extends BladeController implements CacheNames {

	private IPayInterfaceConfigService service;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入post")
	public R<PayInterfaceConfig> detail(PayInterfaceConfigDto dto) {
		PayInterfaceConfig one = service.getOne(Condition.getQueryWrapper(dto));
		return R.data(one);
	}

	@GetMapping("/{appId}/{ifCode}")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入post")
	public R<PayInfoDto> getDetail(@PathVariable(value = "appId") String appId,
								   @PathVariable(value = "ifCode") String ifCode
	) {
		//验证配置是否存在
		PayInterfaceConfig payInterfaceConfig = service.getOne(PayInterfaceConfig.gw()
			.eq(PayInterfaceConfig::getInfoId, appId)
			.eq(PayInterfaceConfig::getIfCode, ifCode)
			.eq(PayInterfaceConfig::getInfoType, 3)
		);
		PayInfoDto dto = new PayInfoDto();
		if(payInterfaceConfig == null){
			return  R.data(201,dto,"支付配置参数为空，请配置");
		}
		//敏感数据的脱敏操作
		if(StringUtil.isNotBlank(payInterfaceConfig.getIfParams())){
			NormalMchParams mchParams = NormalMchParams.factory(ifCode, payInterfaceConfig.getIfParams());
			if(mchParams != null){
				String s = mchParams.deSenData();
				dto.setIfParams(JSON.parseObject(s, IfParams.class));
			}
		}

		dto.setIfCode(ifCode);
		dto.setInfoId(payInterfaceConfig.getInfoId());
		dto.setRemark(payInterfaceConfig.getRemark());
		dto.setStatus(payInterfaceConfig.getStatus());
		//返回

		return R.data(dto);
	}

	/**
	 * 分页 岗位表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入post")
	public R<IPage<PayInterfaceConfig>> list(PayInterfaceConfigDto dto, Query query) {
		IPage<PayInterfaceConfig> pages = service.page(Condition.getPage(query), Condition.getQueryWrapper(dto));
		return R.data(pages);
	}

	/**
	 * 新增 岗位表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入post")
	public R save(@Valid @RequestBody PayInterfaceConfigDto dto) {
		return R.status(service.save(dto));
	}

	/**
	 * 修改 岗位表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入post")
	public R update(@Valid @RequestBody PayInterfaceConfigDto dto) {
		return R.status(service.updateById(dto));
	}

	/**
	 * 新增或修改 岗位表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入post")
	public R submit(@Valid @RequestBody PayInterfaceConfigDto dto) {
		return R.status(service.saveOrUpdate(dto));
	}

	@PostMapping("/payOnSubmit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入post")
	public R payOnSubmit(@Valid @RequestBody PayInfoDto dto) {
		return R.status(service.payOnSubmit(dto));
	}


	/**
	 * 删除 岗位表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(service.deleteLogic(Func.toLongList(ids)));
	}

}
