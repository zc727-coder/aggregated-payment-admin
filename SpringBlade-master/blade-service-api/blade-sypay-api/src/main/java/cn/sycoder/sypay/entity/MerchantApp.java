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
@TableName("t_mch_app")
public class MerchantApp extends BaseEntity {
	public static final LambdaQueryWrapper<MerchantApp> gw(){
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
	@TableField("app_id")
	private String appId;

	@ApiModelProperty(value = "商户名称")
	@TableField("app_name")
	private String appName;

	@ApiModelProperty(value = "商户简称")
	@TableField("merchant_no")
	private String merchantNo;

	@ApiModelProperty(value = "服务商号")
	@TableField("app_secret")
	private String appSecret;



	@ApiModelProperty(value = "备注")
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
