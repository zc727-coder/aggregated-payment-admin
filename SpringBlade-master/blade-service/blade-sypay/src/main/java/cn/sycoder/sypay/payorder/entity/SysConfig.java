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

import java.util.Date;

/**
 * 实体类
 *
 * @author Chill
 */
@Data
@TableName("t_sys_config")
@EqualsAndHashCode(callSuper = true)
public class SysConfig extends BaseEntity {
	public static final LambdaQueryWrapper<SysConfig> gw() {
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
	@TableField(value = "config_key")
	@JsonSerialize(using = ToStringSerializer.class)
	private String configKey;

	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "config_name")
	private String configName;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "config_desc")
	private String configDesc;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "group_key")
	private String groupKey;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "group_name")
	private String groupName;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "config_val")
	private String configVal;
	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "type")
	private String type;	@ApiModelProperty(value = "主键商户号")
	@TableField(value = "sort_num")
	private Long sortNum;

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
