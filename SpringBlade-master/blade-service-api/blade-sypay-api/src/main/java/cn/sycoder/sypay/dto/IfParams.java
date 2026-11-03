package cn.sycoder.sypay.dto;

import lombok.Data;

@Data
public class IfParams {
	String alipayPublicKey;
	String appId;
	String privateKey;
	Integer sandbox;
	String signType;
	String useCert;

}
