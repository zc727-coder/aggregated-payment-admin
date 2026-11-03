package cn.sycoder.sypay.dto;

import cn.sycoder.sypay.entity.PayInterfaceDefine;
import cn.sycoder.sypay.entity.PayWay;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class PayInterfaceDefineDto extends PayInterfaceDefine {
	JSONArray checkedCodes;
}
