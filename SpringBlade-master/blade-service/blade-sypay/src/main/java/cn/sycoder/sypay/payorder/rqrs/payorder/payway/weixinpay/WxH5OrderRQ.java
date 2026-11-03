package cn.sycoder.sypay.payorder.rqrs.payorder.payway.weixinpay;

import cn.sycoder.contants.CS;
import cn.sycoder.sypay.payorder.rqrs.payorder.CommonPayDataRQ;
import lombok.Data;

/*
 * 支付方式： WX_H5
 */
@Data
public class WxH5OrderRQ extends CommonPayDataRQ {

    /** 构造函数 **/
    public WxH5OrderRQ() {
        this.setWayCode(CS.PAY_WAY_CODE.WX_H5);
    }

}
