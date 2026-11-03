package cn.sycoder.sypay.dto;

import lombok.Data;

@Data
public class PayInfoDto {
	String ifCode;
	String infoId;
	String merchantNo;
	String remark;
	Integer status;
	IfParams ifParams;

}
