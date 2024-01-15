package com.rara.dividend.service.impl;

import com.rara.dividend.exception.impl.AlreadyExistUserException;
import com.rara.dividend.exception.impl.NotMatchPasswordException;
import com.rara.dividend.model.Auth.SignIn;
import com.rara.dividend.model.Auth.SignUp;
import com.rara.dividend.model.MemberEntity;
import com.rara.dividend.persist.repository.MemberRepository;
import com.rara.dividend.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return  memberRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(username + " 회원을 찾을 수 없습니다."));
 	}

	@Override
	 public MemberEntity register(SignUp member) {
		boolean exists = memberRepository.existsByUsername(member.getUsername());
		if (exists) {
			throw new AlreadyExistUserException();
		}
		member.setPassword(passwordEncoder.encode(member.getPassword()));

		 return memberRepository.save(member.toEntity());
	}

	 // 로그인시 검증
	 @Override
	public MemberEntity authenticate(SignIn member) {

		var user = memberRepository.findByUsername(member.getUsername())
											.orElseThrow(() -> new UsernameNotFoundException("존재하지 않은 ID 입니다."));
		if(!passwordEncoder.matches(member.getPassword(), user.getPassword())) {
			throw new NotMatchPasswordException();
		}

		return user;
	 }
}
