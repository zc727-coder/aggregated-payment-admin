package cn.sycoder.controller;

import cn.hutool.core.lang.UUID;
import cn.sycoder.config.OssFileConfig;
import cn.sycoder.service.IOssService;
import cn.sycoder.utils.FileKit;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("ossFiles")
public class OssFileController {

	@Autowired
	IOssService service;

	@PostMapping("/{bizType}")
	public R fileUpload(@RequestParam("file") MultipartFile file,
						@PathVariable("bizType") String bizType) {
		if(file == null){
			return R.fail("选择文件不存在");
		}
		//判断文件类型的业务逻辑 //头像 卡片背景图片
		OssFileConfig ossFileConfig = OssFileConfig.getOssFileConfigByBizType(bizType);
		if(ossFileConfig == null){
			return R.fail("类型不支持");
		}
		//判断文件是否支持
		String suffix = FileKit.getFileSuffix(file.getOriginalFilename(), false);
		if(!ossFileConfig.isAllowFileSuffix(suffix)){
			return R.fail("文件后缀不支持");
		}
		//判断文件大小是否超过限制
		if(!ossFileConfig.isMaxSizeLimit(file.getSize())){
			return R.fail("文件大小超过限制");
		}
		//给出文件的存储新地址
		String saveDirAdnFileName = bizType + "/" + UUID.fastUUID() + "." + suffix;
		//文件上传
		String url = service.upload(ossFileConfig.getOssSavePlaceEnum(), file, saveDirAdnFileName);
		return R.data(url);
	}
}
