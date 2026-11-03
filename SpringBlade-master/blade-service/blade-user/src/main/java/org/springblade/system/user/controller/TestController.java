package org.springblade.system.user.controller;

import cn.sycoder.sypay.entity.Test;
import cn.sycoder.sypay.feign.TestClient;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {

	@Autowired
	private TestClient client;
	@GetMapping("/test-detail")
	public R<Test> blogDetail(Integer id) {
		R<Test> result = client.detail(id);
		return result;
	}
}
