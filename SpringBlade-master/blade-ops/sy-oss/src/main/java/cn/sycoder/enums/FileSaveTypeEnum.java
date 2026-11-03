package cn.sycoder.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileSaveTypeEnum {
	ALIYUN_OSS("aliyun-oss"),//存储到oss
	LOCAL("local");//本地存储

	String saveType;
}
