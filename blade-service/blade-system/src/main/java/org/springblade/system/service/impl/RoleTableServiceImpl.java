package org.springblade.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.system.entity.RoleTable;
import org.springblade.system.mapper.RoleTableMapper;
import org.springblade.system.service.IRoleTableService;
import org.springframework.stereotype.Service;

@Service
public class RoleTableServiceImpl extends ServiceImpl<RoleTableMapper,RoleTable> implements IRoleTableService {
}
