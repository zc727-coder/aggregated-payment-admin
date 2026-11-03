package cn.sycoder.service;

import cn.sycoder.config.OssYmlConfig;
import cn.sycoder.enums.FileSaveTypeEnum;
import cn.sycoder.enums.OssSavePlaceEnum;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@ConditionalOnProperty(name="isys.oss.service-type",havingValue = "local")
public class LocalFileService implements IOssService{

	@Autowired
	private OssYmlConfig ossYmlConfig;

	@Override
	public String upload(OssSavePlaceEnum type, MultipartFile file, String saveDirAndFileName) {
		String savePath = type == OssSavePlaceEnum.PUBLIC ? ossYmlConfig.getOss().getFilePublicPath() : ossYmlConfig.getOss().getPrivatePath();
		File saveFile = new File(savePath + File.separator + saveDirAndFileName);
		File dir = saveFile.getParentFile();
		if(!dir.exists()){
			dir.mkdirs();
		}
		try {
			file.transferTo(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//nginx
		return ossYmlConfig.getOss().getUrl()+"/"+saveDirAndFileName;
	}

	@Override
	public boolean downloadFile(OssSavePlaceEnum placeEnum, String source, String target) {
		return false;
	}
}
