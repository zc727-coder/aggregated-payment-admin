package cn.sycoder.service;

import cn.sycoder.enums.FileSaveTypeEnum;
import cn.sycoder.enums.OssSavePlaceEnum;
import org.springframework.web.multipart.MultipartFile;

public interface IOssService {

	String upload(OssSavePlaceEnum type,MultipartFile file, String saveDirAndFileName);

	boolean downloadFile(OssSavePlaceEnum placeEnum, String source, String target);
}
