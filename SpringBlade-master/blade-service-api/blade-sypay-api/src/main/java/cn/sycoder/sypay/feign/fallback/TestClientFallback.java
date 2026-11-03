package cn.sycoder.sypay.feign.fallback;

import cn.sycoder.sypay.entity.Test;
import cn.sycoder.sypay.feign.TestClient;
import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Component;
@Component
public class TestClientFallback implements TestClient {
	@Override
	public R<Test> detail(Integer id) {
		Test test = new Test();
		test.setId(1L);
		test.setName("FallBack Success");
		return R.data(test);
	}
}
