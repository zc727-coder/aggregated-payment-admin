package cn.sycoder.sypay.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("t_mch_info")
public class Merchant extends BaseEntity {

	public static final LambdaQueryWrapper<Merchant> gw(){
		return new LambdaQueryWrapper<>();
	}

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	@ApiModelProperty(value = "商户号")
	@TableField("merchant_no")
	private String merchantNo;

	@ApiModelProperty(value = "商户名称")
	@TableField("merchant_name")
	private String merchantName;

	@ApiModelProperty(value = "商户简称")
	@TableField("merchant_short_name")
	private String merchantShortName;

	@ApiModelProperty(value = "商户类型")
	private Integer type;

	@ApiModelProperty(value = "服务商号")
	@TableField("server_no")
	private String serverNo;

	@ApiModelProperty(value = "联系人姓名")
	@TableField("contact_name")
	private String contactName;

	@ApiModelProperty(value = "联系人电话")
	@TableField("contact_phone")
	private String contactPhone;

	@ApiModelProperty(value = "联系人邮箱")
	@TableField("contact_email")
	private String contactEmail;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "状态")
	private Integer status;

	@ApiModelProperty(value = "租户信息")
	private String telnet;

	@ApiModelProperty(value = "租户账号信息")
	private String account;

	@TableField("create_user")
	private Long createUser;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@TableField("update_user")
	private Long updateUser;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@TableLogic
	@ApiModelProperty("是否已删除")
	@TableField("is_deleted")
	private Integer isDeleted;
}
