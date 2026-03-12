package org.example.backendpractice.service;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.AuthLoginRequest;
import org.example.backendpractice.controller.dto.AuthLoginResponse;
import org.example.backendpractice.controller.dto.AuthSignUpRequest;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.entity.Member;
import org.example.backendpractice.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // private final JwtTokenProvider jwtTokenProvider;

    public void validateLoginId(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        if(member == null) {
            throw new IllegalArgumentException("이미 존재하는 ID");
        }
    }

    public void SignUp(AuthSignUpRequest request) {
        if(memberRepository.findByMemberId(request.getMemberId()) != null) {
            throw new IllegalArgumentException("이미 존재하는 ID");
        }

        Member member = Member.builder()
                .memberName(request.getMemberName())
                .password(request.getMemberPassword())
                .build();

        Member savedMember = memberRepository.save(member);

    }

//    public AuthLoginResponse login(AuthLoginRequest request) {
//        Member member = memberRepository.findByMemberId((request.getMemberLoginId());
//
//        if(member == null) {
//            throw new IllegalArgumentException("일치하는 회원 없음");
//        }
//        String accessToken = jwtTokenProvider.createToken(member.getMemberId());
//        return AuthLoginResponse.from(member, accessToken);
//    }
}
