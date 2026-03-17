package org.example.backendpractice.service;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.*;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.dao.RefreshTokenRepository;
import org.example.backendpractice.entity.Member;
import org.example.backendpractice.entity.RefreshToken;
import org.example.backendpractice.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
       String refreshToken = jwtTokenProvider.createRefreshToken(member.getLoginId());

       RefreshToken savedToken = refreshTokenRepository.findByMember(member)
               .orElseGet(() -> refreshTokenRepository.save(
                       RefreshToken.builder()
                               .token(refreshToken)
                               .member(member)
                               .build()
               ));

       savedToken.updateToken(accessToken);
       refreshTokenRepository.save(savedToken);

        return AuthLoginResponse.from(member, accessToken, refreshToken);
    }

    @Override
    public TokenReissueResponse reissue(TokenReissueRequest request) {
        String refreshToken = request.getRefreshToken();

        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token");
        }

        String loginId = jwtTokenProvider.getLoginId(refreshToken);
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        RefreshToken savedToken = refreshTokenRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 refresh token"));
        if(!savedToken.getToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token 불일치");
        }

        String newAccessToken = jwtTokenProvider.createToken(member.getLoginId());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(member.getLoginId());

        savedToken.updateToken(newRefreshToken);
        refreshTokenRepository.save(savedToken);

        return TokenReissueResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}