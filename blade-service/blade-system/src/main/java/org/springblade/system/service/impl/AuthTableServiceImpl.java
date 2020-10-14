package org.springblade.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.system.entity.AuthTable;
import org.springblade.system.mapper.AuthTableMapper;
import org.springblade.system.service.IAuthTableService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthTableServiceImpl extends ServiceImpl<AuthTableMapper,AuthTable> implements IAuthTableService {

	@Override
	public void customSaveBatch(List<Map> list) {
		List<AuthTable> beans=new ArrayList<AuthTable>();
		for (Map map : list){
			List<AuthTable> authTable=baseMapper.selectByParam(map);
			if(authTable==null||authTable.size()==0){
				AuthTable bean=BeanUtil.toBean(map,AuthTable.class);
				beans.add(bean);
			}
		}
		this.saveBatch(beans);
	}

	@Override
	public boolean selectByParam(Map param) {
		List<AuthTable> authTable=baseMapper.selectByParam(param);
		return authTable.size()>0;
	}

	@Override
	public List<AuthTable> getAll() {
		Map param=new HashMap();
		return baseMapper.selectByParam(param);
	}

	@Override
	public List<AuthTable> getAuthTableByRoleId(Long id) {
		Map param=new HashMap();
		param.put("roleId",id);
		return baseMapper.getTableInfo(param);
	}
}
