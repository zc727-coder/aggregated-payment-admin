package cn.sycoder.sypay.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("t_pay_interface_define")
public class PayInterfaceDefine extends BaseEntity {
	public static final LambdaQueryWrapper<PayInterfaceDefine> gw(){
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
	@TableField("if_code")
	private String ifCode;

	@ApiModelProperty(value = "商户名称")
	@TableField("if_name")
	private String ifName;

	@ApiModelProperty(value = "商户简称")
	@TableField("is_mch_mode")
	private Integer isMchMode;
	@ApiModelProperty(value = "商户简称")
	@TableField("is_isv_mode")
	private Integer isIsvMode;
	@ApiModelProperty(value = "商户简称")
	@TableField("config_page_type")
	private Integer configPageType;
	@ApiModelProperty(value = "商户名称")
	@TableField("isv_params")
	private String isvParams;

	@ApiModelProperty(value = "商户名称")
	@TableField("isvsub_mch_params")
	private String isvsubMchParams;
	@ApiModelProperty(value = "商户名称")
	@TableField("normal_mch_params")
	private String normalMchParams;
	@ApiModelProperty(value = "商户名称")
	@TableField("way_codes")
	private String wayCodes;
	@ApiModelProperty(value = "商户名称")
	@TableField("icon")
	private String icon;
	@ApiModelProperty(value = "商户名称")
	@TableField("bg_color")
	private String bgColor;

	@ApiModelProperty(value = "商户名称")
	@TableField("remark")
	private String remark;

	@ApiModelProperty(value = "状态")
	private Integer status;


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
