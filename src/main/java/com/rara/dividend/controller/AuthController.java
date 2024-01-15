package com.rara.dividend.controller;

import com.rara.dividend.model.Auth.SignIn;
import com.rara.dividend.model.Auth.SignUp;
import com.rara.dividend.security.TokenProvider;
import com.rara.dividend.service.impl.MemberServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberServiceImpl memberServiceImpl;
	private final TokenProvider tokenProvider;

	@ApiOperation(value = "회원가입")
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUp request) {
		var result = memberServiceImpl.register(request);

		return ResponseEntity.ok(result);
	}

	@ApiOperation(value = "로그인")
	@PostMapping("/signin")
	public ResponseEntity<?> signin(@RequestBody SignIn request) {

		var member = memberServiceImpl.authenticate(request);
		String token = tokenProvider.generateToken(member.getUsername(), member.getRoles());
		log.info("user login -> " + request.getUsername());

		return ResponseEntity.ok(token);
	}
}
