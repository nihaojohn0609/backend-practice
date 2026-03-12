package org.example.backendpractice.dao;

import org.example.backendpractice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(Long memberId);
    boolean existsByMemberLoginId(String memberId);
}
