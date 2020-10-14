package org.springblade.system.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@ApiModel(value = "RoleTableVO对象", description = "RoleTableVO对象")
public class RoleTableVO {
	@ApiModelProperty(value = "角色id")
	private Long roleId;

	@ApiModelProperty(value = "tableIds集合")
	private List<Long> tableIds;
}
