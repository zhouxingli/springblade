package org.springblade.auth.feign;

import org.springblade.auth.entity.Result;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
	value = AppConstant.APPLICATION_AUTH_NAME
)
public interface IAuthClient {
	@PostMapping("/getToken")
	Result<String> getToken(@RequestParam(value = "grantType", required = false) String grantType,
					@RequestParam(value = "client_id", required = false) String client_id,
					@RequestParam(value = "client_secret", required = false) String client_secret);
}
