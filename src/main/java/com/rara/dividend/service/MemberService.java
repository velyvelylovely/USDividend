package com.rara.dividend.service;

import com.rara.dividend.model.Auth.SignIn;
import com.rara.dividend.model.Auth.SignUp;
import com.rara.dividend.model.MemberEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {

	@Override
	UserDetails loadUserByUsername(String username);

	 MemberEntity register(SignUp member);

	 // 로그인시 검증
	 MemberEntity authenticate(SignIn member);
}
