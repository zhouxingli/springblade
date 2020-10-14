package org.springblade.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.system.entity.ClientRole;
import org.springblade.system.mapper.ClientRoleMapper;
import org.springblade.system.service.IClientRoleService;
import org.springframework.stereotype.Service;

@Service
public class ClientRoleServiceImpl extends ServiceImpl<ClientRoleMapper,ClientRole> implements IClientRoleService {

}
