package cn.sycoder.sypay.payorder.controller;

import cn.sycoder.sypay.entity.Merchant;
import cn.sycoder.sypay.entity.Test;
import cn.sycoder.sypay.payorder.dto.UnifiedOrder;
import cn.sycoder.sypay.payorder.rqrs.payorder.UnifiedOrderRQ;
import cn.sycoder.sypay.payorder.service.IPayOrderService;
import cn.sycoder.sypay.service.ITestService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.secure.annotation.PreAuth;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("pay/order")
@Slf4j
public class PayOrderController {

	@Autowired
	IPayOrderService service;

	@PostMapping("/api/pay/unifiedOrder")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入post")
	public R unifiedOrder( @RequestBody UnifiedOrderRQ dto) {
		return service.pay(dto);
	}


}
