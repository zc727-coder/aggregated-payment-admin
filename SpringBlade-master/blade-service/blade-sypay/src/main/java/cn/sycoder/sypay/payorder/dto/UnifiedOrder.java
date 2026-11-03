package cn.sycoder.sypay.payorder.dto;

import lombok.Data;

@Data
public class UnifiedOrder {
	String amount;
	String appId;
	String authCode;
	String body;
	String mchNo;
	String mchOrderNo;
	String orderTitle;
	String subject;
	String wayCode;

}
