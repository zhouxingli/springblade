package com.example.demo.feign;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IAuthClientFallback implements IAuthClient {

	@Override
	public Boolean getToken(String grantType, String client_id, String client_secret) {
		return false;
	}
}
