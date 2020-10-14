package org.springblade.auth.feign;



import lombok.AllArgsConstructor;
import org.springblade.auth.entity.Result;
import org.springblade.auth.granter.ClientTokenGranter;
import org.springblade.auth.granter.TokenParameter;
import org.springblade.auth.utils.TokenUtil;
import org.springblade.core.secure.AuthInfo;
import org.springblade.core.secure.provider.IClientDetails;
import org.springblade.system.entity.ClientInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@AllArgsConstructor
public class AuthClient implements IAuthClient {
	private ClientTokenGranter clientTokenGranter;

	@Override
	@PostMapping("/getToken")
	public Result getToken(String grantType, String client_id, String client_secret) {
		Result<String> result=new Result();
		TokenParameter tokenParameter = new TokenParameter();
		tokenParameter.getArgs().set("clientId", client_id).set("clientSecret",client_secret);
		ClientInfo details =clientTokenGranter.grant(tokenParameter);
		if(details==null || details.getAuthClient()==null ||details.getAuthClient().getId()==null){
			result.setSuccess(false);
			result.setData("");
			return result;
		}
		AuthInfo info=TokenUtil.createAuthInfo(details);
		//根据获取的客户端信息生成认证信息
		result.setSuccess(true);
		result.setData(info.getAccessToken());
		return result;
	}
}
