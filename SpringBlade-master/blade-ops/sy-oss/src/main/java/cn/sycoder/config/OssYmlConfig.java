package cn.sycoder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "isys")
public class OssYmlConfig {
	@NestedConfigurationProperty//指定该属性为嵌套值
	private Oss oss;

	@Data
	public static class Oss{
		private String filePublicPath;//公共读
		private String privatePath;//私有读
		private String serviceType;//服务类型
		private String fileRootPath;//存储的根路径
		private String url;//服务器的访问路径
	}
}
