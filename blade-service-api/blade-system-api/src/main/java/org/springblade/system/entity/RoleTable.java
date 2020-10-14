package org.springblade.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("blade_role_table")
@ApiModel(value = "RoleTable对象", description = "RoleTable对象")
public class RoleTable implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 角色id
	 */
	@ApiModelProperty(value = "角色id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long roleId;

	/**
	 * 表名
	 */
	@ApiModelProperty(value = "表名")
	private Long tableId;
	/**
	* 查询权限
	*/
	@ApiModelProperty(value = "查询权限")
	private Boolean hasSelect;

	/**
	 * 更新权限
	 */
	@ApiModelProperty(value = "更新权限")
	private Boolean hasUpdate;

	/**
	 * 删除权限
	 */
	@ApiModelProperty(value = "删除权限")
	private Boolean hasDelete;
}
