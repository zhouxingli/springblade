package org.springblade.system.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "ClientRoleVO对象", description = "ClientRoleVO对象")
public class ClientRoleVO {
	@ApiModelProperty(value = "客户端id")
	private Long clientId;

	@ApiModelProperty(value = "roleIds集合")
	private List<Long> roleIds;
}
