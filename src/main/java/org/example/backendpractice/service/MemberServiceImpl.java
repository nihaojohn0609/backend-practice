package org.example.backendpractice.service;


import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.AuthPasswordChangeRequest;
import org.example.backendpractice.controller.dto.MemberResponse;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;


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
    public void changePassword(Long memberId, AuthPasswordChangeRequest request) {
        Member member = memberRepository.findByMemberId(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        member.changePassword(request.getOldPassword(),  request.getNewPassword());
        memberRepository.save(member);
    }
}
