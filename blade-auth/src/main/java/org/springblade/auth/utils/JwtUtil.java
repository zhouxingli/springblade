package org.springblade.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springblade.core.launch.constant.TokenConstant;
import org.springblade.core.secure.TokenInfo;
import org.springblade.core.secure.provider.IClientDetails;
import org.springblade.core.tool.utils.Charsets;
import org.springblade.system.entity.AuthClient;
import org.springblade.system.entity.ClientInfo;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class JwtUtil {
	private static final String HEADER = "blade-client";
	private static final String BEARER = "bearer";
	private static String BASE64_SECURITY;


	static {
		BASE64_SECURITY = Base64.getEncoder().encodeToString("Blade".getBytes(Charsets.UTF_8));
	}
	/*
	 * 生成token
	 *
	 * */
	public static TokenInfo createToken(Map<String, String> client, AuthClient authClient, String audience, String issuer, String tokenType) {
		// 使用HS256加密算法
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken").setIssuer(issuer).setAudience(audience).signWith(signatureAlgorithm, signingKey);
		client.forEach(builder::claim);
		builder.claim(TokenConstant.CLIENT_ID, authClient.getClientId());
		long expireMillis=(long)(authClient.getAccessTokenValidity().intValue() * 1000);
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + expireMillis;
		Date exp = new Date(expMillis);
		Date now = new Date(nowMillis);
		builder.setExpiration(exp).setNotBefore(now);
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setToken(builder.compact());
		tokenInfo.setExpire((int)expireMillis / 1000);
		return tokenInfo;
	}



	public static Claims checkJWT(String token) {
		try {
			byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
			Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
			final Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
			return claims;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getClientId(String token){
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
		Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
		return claims.get("client_id").toString();
	}

	public static String getClientRole(String token){
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
		Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
		return claims.get("rol").toString();
	}

	public static boolean isExpiration(String token){
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
		Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
		return claims.getExpiration().before(new Date());
	}

}
