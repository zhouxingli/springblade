/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.system.entity.*;
import org.springblade.system.mapper.AuthClientMapper;
import org.springblade.system.mapper.AuthTableMapper;
import org.springblade.system.service.IAuthClientService;
import org.springblade.system.service.IClientRoleService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  服务实现类
 *
 * @author Chill
 */
@Service
public class AuthClientServiceImpl extends BaseServiceImpl<AuthClientMapper, AuthClient> implements IAuthClientService {
	@Resource
	private RedisUtil redisUtil;
	@Resource
	private AuthTableMapper authTableMapper;
	@Resource
	private IClientRoleService clientRoleService;
	@Override
	public ClientInfo getClientInfo(String clientId,String clientSecret) {
		ClientInfo info=new ClientInfo();
		AuthClient authClient= baseMapper.getClient(clientId,clientSecret);
		if(authClient!=null){
			List<Role> list=baseMapper.getRoleInfo(authClient.getId());
			List<String> roleIds=new ArrayList<String>();
			List<String> roleAlias=new ArrayList<String>();
			list.forEach(i -> {
				roleIds.add(i.getId().toString());
				roleAlias.add(i.getRoleAlias());
			});
			info.setRoles(roleAlias);
			//List<String> permissions=new ArrayList<String>();
			Boolean hasKey=redisUtil.hasKey(authClient.getClientId()+"_permissions");
			if(!hasKey){
				Map param=new HashMap();
				param.put("roleIds",roleIds);
				List<AuthTable> tableInfo=authTableMapper.getTableInfo(param);
				if(tableInfo!=null&&tableInfo.size()>0){
					Map<String,List<AuthTable>> resultMap=tableInfo.stream().collect(Collectors.groupingBy(AuthTable::getServerId));
					Set<String> keys=resultMap.keySet();
					Iterator<String> iterator=keys.iterator();
					while (iterator.hasNext()){
						String key=iterator.next();
						List<AuthTable> authTables=resultMap.get(key);
						List<String> permissions=new ArrayList<String>();
						authTables.forEach(e -> {
							permissions.add(e.getTableName());
						});
						redisUtil.hset(authClient.getClientId()+"_permissions",key,permissions);
					}
				}
			}
		}
		info.setAuthClient(authClient);
		return info;
	}

	@Override
	public List<Role> getRoleInfo(Long clientId) {
		return baseMapper.getRoleInfo(clientId);
	}

	@Override
	public boolean grantRole(Long clientId, List<Long> roleIds) {
		clientRoleService.remove(Wrappers.<ClientRole>query().in("CLIENT_ID",clientId));
		List<ClientRole> list=new ArrayList();
		roleIds.forEach(roleId -> {
			ClientRole clientRole=new ClientRole();
			clientRole.setClientId(clientId);
			clientRole.setRoleId(roleId);
			list.add(clientRole);
		});
		return clientRoleService.saveBatch(list);
	}
}
