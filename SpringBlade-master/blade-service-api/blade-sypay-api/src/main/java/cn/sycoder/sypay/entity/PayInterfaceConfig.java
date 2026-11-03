package cn.sycoder.sypay.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_pay_interface_config")
public class PayInterfaceConfig extends BaseEntity {
	public static final LambdaQueryWrapper<PayInterfaceConfig> gw(){
		return new LambdaQueryWrapper<>();
	}
	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	@ApiModelProperty(value = "商户简称")
	@TableField("info_type")
	private Integer infoType;

	@ApiModelProperty(value = "商户号")
	@TableField("info_id")
	private String infoId;

	@ApiModelProperty(value = "商户名称")
	@TableField("if_code")
	private String ifCode;
	@ApiModelProperty(value = "商户名称")
	@TableField("if_params")
	private String ifParams;
	@ApiModelProperty(value = "商户名称")
	@TableField("if_rate")
	private BigDecimal ifRate;

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
