package com.rara.dividend.persist.repository;

import com.rara.dividend.model.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	// 아이디를 기준으로 회원정보 조회
	Optional<MemberEntity> findByUsername(String username);

	// 이미 존재하는 아이디인지 확인
	boolean existsByUsername(String username);
}
