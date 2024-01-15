package com.rara.dividend.security;

import com.rara.dividend.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;  // 1hour
	private static final String KEY_ROLES = "roles";
	private final MemberService memberService;

	@Value("{spring.jwt.secret}")
	private String secretKey;

	public String generateToken(String username, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put(KEY_ROLES, roles);

		var now = new Date();
		var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);         //토큰 생성 1시간 후까지 유효함

		return Jwts.builder()
					.setClaims(claims)
					.setIssuedAt(now)               // 토큰 생성 시간
					.setExpiration(expiredDate)     // 토큰 만료 시간
					.signWith(SignatureAlgorithm.HS512, secretKey)  // 사용할 암호화 알고리즘, 비밀키
					.compact();
	}

	public Authentication getAuthentication(String jwt) {

		UserDetails userDetails = memberService.loadUserByUsername(this.getUsername(jwt));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(String token) {
		return parseClaims(token).getSubject();
	}

	public boolean validateToken(String token) {
		if (!StringUtils.hasText(token)) {
			return false;
		}
		var claims = parseClaims(token);
		return !claims.getExpiration().before(new Date());     // 만료시간이 지금보다 전인지 확인
	}


	private Claims parseClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
 		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
