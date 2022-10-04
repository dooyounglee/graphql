package com.doo.graphql.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final UserDetailsService userDetailsService;
	
	@Value("${springboot.jwt.secret}")
	private String secretKey = "secretKey";
	private final long tokenvalidMillisecond = 1000L * 60 * 60; // 1초 -> 1분 -> 1시간
	
	@PostConstruct
	protected void init() {
		log.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
		log.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
	}
	
	public String createToken(String userUid, List<String> roles) {
		log.info("[createToken] 토큰 생성 시작");
		Claims claims = Jwts.claims().setSubject(userUid);
		claims.put("roles", roles);
		Date now = new Date();
		
		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + tokenvalidMillisecond))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact()
				;
		
		log.info("[createToken] 토큰 생성 완료");
		return token;
	}
	
	public Authentication getAuthentication(String token) {
		log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
		log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails Username : {}", userDetails.getUsername());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	public String getUsername(String token) {
		log.info("[getUsername] 토큰 기반 호원 구별 정보 추출");
		String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		log.info("[getUsername] 토큰 기반 호원 구별 정보 추출 완료, info : {}", info);
		return info;
	}
	
	public String resolveToken(HttpServletRequest servletRequest) {
		log.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
		return servletRequest.getHeader("X-AUTH-TOKEN");
	}
	
	public boolean validateToken(String token) {
		log.info("[validateToken] 토큰 유효 체크 시작");
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			log.info("[validateToken] 토큰 유효 체크 예외 발생");
			return false;
		}
	}
}
