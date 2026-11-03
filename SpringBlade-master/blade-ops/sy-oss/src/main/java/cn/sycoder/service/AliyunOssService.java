package cn.sycoder.service;

import cn.sycoder.config.AliyunOssYmlConfig;
import cn.sycoder.enums.OssSavePlaceEnum;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Service
@ConditionalOnProperty(name="isys.oss.service-type",havingValue = "aliyun-oss")
public class AliyunOssService implements IOssService{

	@Autowired
	AliyunOssYmlConfig config;

	private OSS ossClient;

	@PostConstruct
	public void init(){
		ossClient = new OSSClientBuilder().build(config.getEndpoint(), config.getAccessKeyId(),config.getAccessKeySecret());
	}

	@Override
	public String upload(OssSavePlaceEnum type, MultipartFile file, String saveDirAndFileName) {
		String bucketName = type == OssSavePlaceEnum.PUBLIC ? config.getPublicBucketName():config.getPrivateBucketName();
		try {
			ossClient.putObject(bucketName, saveDirAndFileName, file.getInputStream());
			if(type == OssSavePlaceEnum.PUBLIC){
				return "https://"+config.getPublicBucketName()+"."+config.getEndpoint()+"/"+saveDirAndFileName;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return saveDirAndFileName;
	}

	@Override
	public boolean downloadFile(OssSavePlaceEnum placeEnum, String source, String target) {
		String bucket = placeEnum == OssSavePlaceEnum.PRIVATE ? config.getPrivateBucketName() : config.getPublicBucketName();
		ossClient.getObject(new GetObjectRequest(bucket,source),new File(target));
		return true;
	}
}
