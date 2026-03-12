package org.example.backendpractice.service;


import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.AuthPasswordChangeRequest;
import org.example.backendpractice.controller.dto.MemberResponse;
import org.example.backendpractice.controller.dto.MemberUpdateRequest;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<MemberResponse> findAllMembers() {
        List<MemberResponse> members = new ArrayList<>();
        List<Member> result = memberRepository.findAll();
        for (Member member : result) {
            MemberResponse response = MemberResponse.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .password(member.getPassword())
                    .build();
            members.add(response);
        }
        return members;
    }

    @Override
    public MemberResponse findById(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        return MemberResponse.from(member.get());
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long memberId, String loginId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원"));

        if (!member.getLoginId().equals(loginId)) {
            throw new IllegalArgumentException("수정 권한 없음");
        }

        String encodedPassword = null;
        if(request.getPassword() != null && !request.getPassword().isBlank()) {
            encodedPassword = passwordEncoder.encode(request.getPassword());
        }

        member.updateMemberInfo(request.getMemberName(), encodedPassword);
        return MemberResponse.from(member);
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId, String loginId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원"));

        if (!member.getLoginId().equals(loginId)) {
            throw new IllegalArgumentException("수정 권한 없음");
        }
        memberRepository.delete(member);
    }
}