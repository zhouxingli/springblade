package org.springblade.system.controller;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.AnnotationUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.system.entity.AuthTable;
import org.springblade.system.service.IAuthTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/authTable")
@Api(value = "表名", tags = "表名")
@AllArgsConstructor
public class AuthTableController extends BladeController {


	private IAuthTableService authTableService;
	/*
	* 获取需要配置权限的实体类表名
	* */
	@GetMapping("/getAllTable")
	@ApiOperation(value = "实体类表名", notes = "实体类表名")
	public R getTableNames(){
		List<AuthTable> list=authTableService.getAll();
		return R.data(list);
	}

	@PostMapping("/getTableByRole")
	@ApiOperation(value = "实体类表名", notes = "传入角色id")
	public R getTableByRole(@RequestParam Long roleId){
		List<AuthTable> list=authTableService.getAuthTableByRoleId(roleId);
		return R.data(list);
	}
}
