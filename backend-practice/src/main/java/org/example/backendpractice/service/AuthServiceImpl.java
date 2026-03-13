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
    private final JwtTokenProvider jwtTokenProvider;

    public void validateLoginId(String loginId) {
        if(memberRepository.findByLoginId(loginId).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ID");
        }
    }

    public void SignUp(AuthSignUpRequest request) {
        if(memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ID");
        }

        Member member = Member.builder()
                .loginId(request.getLoginId())
                .memberName(request.getMemberName())
                .password(passwordEncoder.encode(request.getMemberPassword()))
                .build();

        memberRepository.save(member);

    }

   public AuthLoginResponse login(AuthLoginRequest request) {
       Member member = memberRepository.findByLoginId(request.getLoginId())
               .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));

       if(!passwordEncoder.matches(request.getMemberPassword(), member.getPassword())) {
           throw new IllegalArgumentException("비밀번호 틀림");
       }

        String accessToken = jwtTokenProvider.createToken(member.getLoginId());
        return AuthLoginResponse.from(member, accessToken);
    }
}
