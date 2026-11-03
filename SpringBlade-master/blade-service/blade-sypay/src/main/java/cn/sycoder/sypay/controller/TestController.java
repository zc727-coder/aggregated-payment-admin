package cn.sycoder.sypay.controller;

import cn.sycoder.sypay.entity.Test;
import cn.sycoder.sypay.service.ITestService;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.secure.annotation.PreAuth;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Slf4j
public class TestController {

	@Autowired
	ITestService testService;

	@GetMapping("info")
	@PreAuth("hasRole('administrator')")
//	@Cacheable(cacheNames = "demo-info", key = "#name")
	public R info(@RequestBody Test test) {
		return R.data(testService.submit(test));
	}

	@GetMapping("test")
	public R<Test> getById(Long id){
		return R.data(testService.getById(id));
	}

	@GetMapping("count")
	@PreAuth("permitAll()")
	public R<Integer> count(Integer cnt) {
		return R.data(cnt * 10);
	}


}
