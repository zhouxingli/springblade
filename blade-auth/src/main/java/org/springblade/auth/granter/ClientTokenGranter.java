package org.springblade.auth.granter;

import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springblade.core.secure.provider.ClientDetails;
import org.springblade.core.secure.provider.IClientDetails;
import org.springblade.core.secure.provider.IClientDetailsService;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.system.entity.ClientInfo;
import org.springblade.system.feign.ISysClient;
import org.springblade.system.user.entity.UserInfo;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClientTokenGranter{
	public static final String GRANT_TYPE = "client_credentials";

	private IClientDetailsService clientDetailsService;

	private ISysClient sysClient;

	public ClientInfo grant(TokenParameter tokenParameter) {
		String clientId = tokenParameter.getArgs().getStr("clientId");
		String clientSecret = tokenParameter.getArgs().getStr("clientSecret");
		clientSecret= DigestUtil.encrypt(clientSecret);
		ClientInfo clientInfo=sysClient.getClientInfo(clientId,clientSecret).getData();
		if(clientInfo!=null&&clientInfo.getAuthClient()!=null){
			return clientInfo;
		}
		return null;
	}

	private static boolean validateClient(IClientDetails clientDetails, String clientId, String clientSecret) {
		if (clientDetails == null) {
			return false;
		} else {
			String secret= DigestUtil.encrypt(clientSecret);
			return StringUtil.equals(clientId, clientDetails.getClientId()) && StringUtil.equals(secret, clientDetails.getClientSecret());
		}
	}
}
