package org.springblade.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springblade.system.entity.AuthTable;

import java.util.List;
import java.util.Map;

public interface AuthTableMapper extends BaseMapper<AuthTable> {
	List<AuthTable> selectByParam(Map param);

	public List<AuthTable> getTableInfo(Map param);
}
