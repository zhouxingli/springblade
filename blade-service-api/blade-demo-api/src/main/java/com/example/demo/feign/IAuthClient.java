package com.example.demo.feign;

import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.secure.AuthInfo;
import org.springblade.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(
	value = AppConstant.APPLICATION_AUTH_NAME
)
public interface IAuthClient {
	@PostMapping("/getToken")
	Boolean getToken( @RequestParam(value = "grantType",required = false) String grantType,
					  @RequestParam(value = "client_id",required = false) String client_id,
					  @RequestParam(value = "client_secret",required = false) String client_secret);
}
