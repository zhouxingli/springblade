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
package org.springblade.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.auth.entity.Result;
import org.springblade.auth.feign.IAuthClient;
import org.springblade.core.launch.constant.TokenConstant;
import org.springblade.gateway.props.AuthProperties;
import org.springblade.gateway.provider.AuthProvider;
import org.springblade.gateway.provider.ResponseProvider;
import org.springblade.gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鉴权认证
 *
 * @author Chill
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {
	private AuthProperties authProperties;
	private ObjectMapper objectMapper;

	private IAuthClient authClient;
	@SneakyThrows
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		if (isSkip(path)) {
			return chain.filter(exchange);
		}
		ServerHttpResponse resp = exchange.getResponse();
		String headerToken = exchange.getRequest().getHeaders().getFirst(AuthProvider.AUTH_KEY);
		String paramToken = exchange.getRequest().getQueryParams().getFirst(AuthProvider.AUTH_KEY);
		if (StringUtils.isAllBlank(headerToken, paramToken)) {
			//不带token访问并且是客户端访问
			String grantType=exchange.getRequest().getHeaders().getFirst(AuthProvider.GRANT_TYPE);
			if(grantType!=null&&grantType.equals(TokenConstant.GRANT_TYPE)){
				String header=exchange.getRequest().getHeaders().getFirst("Authorization");
				header.replace("Basic%20", "Basic ");
				byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8.name());
				byte[] decoded;
				try {
					decoded = Base64.getDecoder().decode(base64Token);
				} catch (IllegalArgumentException var7) {
					throw new RuntimeException("Failed to decode basic authentication token");
				}
				String tokenMsg = new String(decoded, StandardCharsets.UTF_8.name());
				int index=tokenMsg.indexOf(":");
				if(index>-1){
					String[] client=new String[]{tokenMsg.substring(0, index), tokenMsg.substring(index + 1)};
					Result<String> result=authClient.getToken(grantType,client[0],client[1]);
					if(result.isSuccess()){
						String token=result.getData();
						org.springframework.http.server.reactive.ServerHttpRequest host=exchange.getRequest().mutate().headers(httpHeaders -> {
							httpHeaders.add("Blade-auth","bearer "+token);
						}).build();
						ServerWebExchange build=exchange.mutate().request(host).build();
						return chain.filter(build);
					}else{
						return unAuth(resp, "客户端信息错误，鉴权失败");
					}

				}
			}else{
				return unAuth(resp, "缺失令牌,鉴权失败");
			}
		}
		String auth = StringUtils.isBlank(headerToken) ? paramToken : headerToken;
		String token =JwtUtil.getToken(auth);
		Claims claims = JwtUtil.parseJWT(token);
		if (claims == null) {
			return unAuth(resp, "请求未授权");
		}
		return chain.filter(exchange);
	}

	private boolean isSkip(String path) {
		return AuthProvider.getDefaultSkipUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::contains)
			|| authProperties.getSkipUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::contains);
	}

	private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
		resp.setStatusCode(HttpStatus.UNAUTHORIZED);
		resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		String result = "";
		try {
			result = objectMapper.writeValueAsString(ResponseProvider.unAuth(msg));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
		return resp.writeWith(Flux.just(buffer));
	}

	@Override
	public int getOrder() {
		return -100;
	}

}
