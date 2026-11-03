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
@TableName("t_mch_division_receiver")
@EqualsAndHashCode(callSuper = true)
public class MchDivisionReceiver extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	@ApiModelProperty(value = "主键")
	@TableField(value = "receiver_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiverId;
	@ApiModelProperty(value = "主键")
	@TableField(value = "receiver_group_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiverGroupId;

	@ApiModelProperty(value = "主键")
	@TableField(value = "receiver_alias")
	@JsonSerialize(using = ToStringSerializer.class)
	private String receiverAlias;

	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "receiver_group_name")
	private String receiverGroupName;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "mch_no")
	private String mchNo;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "isv_no")
	private String isvNo;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "app_id")
	private String appId;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "if_code")
	private String ifCode;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "acc_no")
	private String accNo;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "acc_name")
	private String accName;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "relation_type")
	private String relationType;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "relation_type_name")
	private String relationTypeName;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "channel_bind_result")
	private String channelBindResult;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "channel_ext_info")
	private String channelExtInfo;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "division_profit")
	private BigDecimal divisionProfit;

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
	@TableField("bind_success_time")
	private Date bindSuccessTime;

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
