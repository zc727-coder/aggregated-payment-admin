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

import cn.sycoder.sypay.dto.PayWayDto;
import cn.sycoder.sypay.entity.PayWay;
import cn.sycoder.sypay.service.IPayWayService;
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
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 岗位表 控制器
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@RequestMapping("/pay/way")
@Api(value = "岗位表", tags = "岗位表接口")
public class PayWayController extends BladeController implements CacheNames {

	private IPayWayService service;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入post")
	public R<PayWay> detail(PayWayDto dto) {
		PayWay one = service.getOne(Condition.getQueryWrapper(dto));
//		MerchantVo merchantVo = BeanUtil.copyProperties(one, MerchantVo.class);
		return R.data(one);
	}

	@GetMapping("/codeDetail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入post")
	public R<List<PayWay>> codeDetail() {
		List<PayWay> list = service.list();
//		MerchantVo merchantVo = BeanUtil.copyProperties(one, MerchantVo.class);
		return R.data(list);
	}

	/**
	 * 分页 岗位表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入post")
	public R<IPage<PayWay>> list(PayWayDto dto, Query query) {
		IPage<PayWay> pages = service.page(Condition.getPage(query), Condition.getQueryWrapper(dto));
		return R.data(pages);
	}
	/**
	 * 新增 岗位表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入post")
	public R save(@Valid @RequestBody PayWayDto dto) {
		return R.status(service.save(dto));
	}

	/**
	 * 修改 岗位表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入post")
	public R update(@Valid @RequestBody PayWayDto dto) {
		return R.status(service.updateById(dto));
	}

	/**
	 * 新增或修改 岗位表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入post")
	public R submit(@Valid @RequestBody PayWayDto dto) {
		return R.status(service.saveOrUpdate(dto));
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
