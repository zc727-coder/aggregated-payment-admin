package cn.sycoder.sypay.feign;

import cn.sycoder.sypay.constant.SyPayConstant;
import cn.sycoder.sypay.entity.Test;
import cn.sycoder.sypay.feign.fallback.TestClientFallback;
import org.springblade.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
//定义Feign指向的service-id
	value = SyPayConstant.APPLICATION_SYPAY_NAME,
	//定义hystrix配置类
	fallback = TestClientFallback.class
)
public interface TestClient {

	/**
	 * 获取详情
	 *
	 * @param id 主键
	 * @return
	 */
	@GetMapping(SyPayConstant.API_PREFIX+"test/detail")
	R<Test> detail(@RequestParam("id") Integer id);
}
