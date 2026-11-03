/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sycoder.sypay.payorder.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体类
 *
 * @author Chill
 */
@Data
@TableName("t_refund_order")
@EqualsAndHashCode(callSuper = true)
public class RefundOrder extends BaseEntity {
	public static final byte STATE_INIT = 0; //订单生成
	public static final byte STATE_ING = 1; //退款中
	public static final byte STATE_SUCCESS = 2; //退款成功
	public static final byte STATE_FAIL = 3; //退款失败
	public static final byte STATE_CLOSED = 4; //退款任务关闭

	public static final LambdaQueryWrapper<RefundOrder> gw() {
		return new LambdaQueryWrapper<>();
	}

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	@ApiModelProperty(value = "主键")
	@TableField(value = "refund_order_id")
	private String refundOrderId;
	@ApiModelProperty(value = "主键")
	@TableField(value = "pay_order_id")
	private String payOrderId;
	@ApiModelProperty(value = "主键")
	@TableField(value = "channel_pay_order_no")
	private String channelPayOrderNo;

	@ApiModelProperty(value = "主键")
	@TableField(value = "mch_no")
	private String mchNo;
	@ApiModelProperty(value = "主键")
	@TableField(value = "isv_no")
	private String isvNo;
	@ApiModelProperty(value = "主键")
	@TableField(value = "app_id")
	private String appId;
	@ApiModelProperty(value = "主键")
	@TableField(value = "mch_name")
	private String mchName;
	@ApiModelProperty(value = "主键")
	@TableField(value = "mch_refund_no")
	private String mchRefundNo;
	@ApiModelProperty(value = "主键")
	@TableField(value = "way_code")
	private String wayCode;
	@ApiModelProperty(value = "主键")
	@TableField(value = "mch_type")
	private Integer mchType;

	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "currency")
	private String currency;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "client_ip")
	private String clientIp;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "refund_reason")
	private String refundReason;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "channel_order_no")
	private String channelOrderNo;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "err_code")
	private String errCode;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "err_msg")
	private String errMsg;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "channel_extra")
	private String channelExtra;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "notify_url")
	private String notifyUrl;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "ext_param")
	private String extParam;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "pay_amount")
	private BigDecimal payAmount;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "refund_amount")
	private BigDecimal refundAmount;

	@ApiModelProperty("是否已删除")
	@TableField(value = "acc_type")
	private Integer accType;

	@ApiModelProperty("创建人")
	@TableField("create_user")
	private Long createUser;
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty("创建时间")
	@TableField("create_time")
	private Date createTime;
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty("创建时间")
	@TableField("success_time")
	private Date successTime;

	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty("创建时间")
	@TableField("expired_time")
	private Date expiredTime;

	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty("更新时间")
	@TableField("update_time")
	private Date updateTime;

	@ApiModelProperty("更新人")
	@TableField("update_user")
	private Long updateUser;

	@TableLogic
	@ApiModelProperty("是否已删除")
	private Integer isDeleted;

}
