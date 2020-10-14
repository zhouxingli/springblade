package org.springblade.auth.feign;

import org.springblade.auth.entity.Result;
import org.springframework.stereotype.Component;

@Component
public class IAuthClientFallback implements IAuthClient {

	@Override
	public Result<String> getToken(String grantType, String client_id, String client_secret) {
		Result result=new Result();
		result.setSuccess(false);
		return result;
	}
}
