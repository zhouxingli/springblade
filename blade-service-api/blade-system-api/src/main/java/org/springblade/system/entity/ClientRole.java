package org.springblade.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("blade_client_role")
@ApiModel(value = "ClientRole对象", description = "ClientRole对象")
public class ClientRole implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 客户端id
	 */
	@ApiModelProperty(value = "客户端id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long clientId;

	/**
	 * 角色id
	 */
	@ApiModelProperty(value = "角色id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long roleId;
}
