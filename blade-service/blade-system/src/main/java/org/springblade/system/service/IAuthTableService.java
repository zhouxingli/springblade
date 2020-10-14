package org.springblade.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.system.entity.AuthTable;

import java.util.List;
import java.util.Map;

public interface IAuthTableService extends IService<AuthTable>{

	public void customSaveBatch(List<Map> list);

	public boolean selectByParam(Map param);

	public List<AuthTable> getAll();

	public List<AuthTable> getAuthTableByRoleId(Long id);
}
