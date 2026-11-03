package cn.sycoder.sypay.feign;

import cn.sycoder.sypay.constant.SyPayConstant;
import cn.sycoder.sypay.entity.Test;
import cn.sycoder.sypay.service.ITestService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestFeignClientImpl implements TestClient{

	private ITestService service;

	@Override
	@GetMapping(SyPayConstant.API_PREFIX+"test/detail")
	public R<Test> detail(Integer id) {
		int cnt = 100 / 0;
		return R.data(service.getById(id));
	}
}
