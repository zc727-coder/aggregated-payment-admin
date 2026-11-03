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
@TableName("t_pay_order")
@EqualsAndHashCode(callSuper = true)
public class PayOrder extends BaseEntity {
	public static final LambdaQueryWrapper<PayOrder> gw() {
		return new LambdaQueryWrapper<>();
	}

	private static final long serialVersionUID = 1L;


	public static final int STATE_INIT = 0; //订单生成
	public static final int STATE_ING = 1; //支付中
	public static final int STATE_SUCCESS = 2; //支付成功
	public static final int STATE_FAIL = 3; //支付失败
	public static final int STATE_CANCEL = 4; //已撤销
	public static final int STATE_REFUND = 5; //已退款
	public static final int STATE_CLOSED = 6; //订单关闭

	public static final int REFUND_STATE_NONE = 0; //未发生实际退款
	public static final int REFUND_STATE_SUB = 1; //部分退款
	public static final int REFUND_STATE_ALL = 2; //全额退款


	public static final int DIVISION_MODE_FORBID = 0; //该笔订单不允许分账
	public static final int DIVISION_MODE_AUTO = 1; //支付成功按配置自动完成分账
	public static final int DIVISION_MODE_MANUAL = 2; //商户手动分账(解冻商户金额)

	public static final int DIVISION_STATE_UNHAPPEN = 0; //未发生分账
	public static final int DIVISION_STATE_WAIT_TASK = 1; //等待分账任务处理
	public static final int DIVISION_STATE_ING = 2; //分账处理中
	public static final int DIVISION_STATE_FINISH = 3; //分账任务已结束(不体现状态)


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	@ApiModelProperty(value = "主键")
	@TableField(value = "pay_order_id")
	private String payOrderId;
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
	@TableField(value = "mch_type")
	private Integer mchType;
	@ApiModelProperty(value = "主键")
	@TableField(value = "mch_order_no")
	private String mchOrderNo;
	@ApiModelProperty(value = "主键")
	@TableField(value = "if_code")
	private String ifCode;
	@ApiModelProperty(value = "主键")
	@TableField(value = "way_code")
	private String wayCode;
	@ApiModelProperty(value = "主键")
	@TableField(value = "amount")
	private Long amount;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "mch_fee_rate")
	private BigDecimal mchFeeRate;
	@ApiModelProperty(value = "主键")
	@TableField(value = "mch_fee_amount")
	private Long mchFeeAmount;
	@ApiModelProperty(value = "主键")
	@TableField(value = "currency")
	private String currency;
	@ApiModelProperty(value = "主键")
	@TableField(value = "notify_state")
	private Integer notifyState;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "subject")
	private String subject;
	@ApiModelProperty(value = "主键")
	@TableField(value = "channel_order_no")
	private String channelPayOrderNo;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "client_ip")
	private String clientIp;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "body")
	private String body;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "channel_extra")
	private String channelExtra;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "channel_user")
	private String channelUser;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "channel_order_no")
	private String channelOrderNo;
	@ApiModelProperty(value = "主键")
	@TableField(value = "refund_state")
	private Integer refundState;
	@TableField("refund_times")
	private Integer refundTimes;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "refund_amount")
	private Long refundAmount;
	@ApiModelProperty(value = "主键")
	@TableField(value = "division_mode")
	private Integer divisionMode;
	@ApiModelProperty(value = "主键")
	@TableField(value = "division_state")
	private Integer divisionState;
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty("创建时间")
	@TableField("division_last_time")
	private Date divisionLastTime;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "err_code")
	private String errCode;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "err_msg")
	private String errMsg;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "ext_param")
	private String extParam;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "notify_url")
	private String notifyUrl;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "return_url")
	private String returnUrl;
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
	@TableField("create_time")
	private Date createTime;
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty("更新时间")
	@TableField("update_time")
	private Date updateTime;
	@ApiModelProperty("创建人")
	@TableField("create_user")
	private Long createUser;
	@ApiModelProperty("更新人")
	@TableField("update_user")
	private Long updateUser;
	@TableLogic
	@ApiModelProperty("是否已删除")
	private Integer isDeleted;

}
