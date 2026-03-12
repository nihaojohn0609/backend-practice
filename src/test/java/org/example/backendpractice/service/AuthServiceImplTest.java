package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.AuthLoginRequest;
import org.example.backendpractice.controller.dto.AuthLoginResponse;
import org.example.backendpractice.controller.dto.AuthSignUpRequest;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.entity.Member;
import org.example.backendpractice.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 가짜 객체(Mock)를 사용
class AuthServiceImplTest {

    @Mock
    private MemberRepository memberRepository; // 실제 DB 대신 사용할 가짜 DB 객체

    @Mock
    private PasswordEncoder passwordEncoder; // 가짜 암호화 객체

    @Mock
    private JwtTokenProvider jwtTokenProvider; // 가짜 토큰 생성 객체

    @InjectMocks
    private AuthServiceImpl authService; // 가짜 객체들을 주입받아 테스트할 진짜 서비스 객체

    @Test
    @DisplayName("회원가입_성공_테스트")
    void signUp_Success() {
        AuthSignUpRequest request = new AuthSignUpRequest();
        request.setLoginId("testuser");
        request.setMemberName("testuser");
        request.setMemberPassword("1234");

        // DB에 아이디가 존재하지 않는다고 가정
        when(memberRepository.findByLoginId("testuser")).thenReturn(Optional.empty());
        // 암호화된 비밀번호를 반환한다고 가정
        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");

        authService.SignUp(request);

        // memberRepository.save()가 1번 실행되었는지 확인
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입_실패_중복된아이디")
    void signUp_Fail_DuplicateId() {
        AuthSignUpRequest request = new AuthSignUpRequest();
        request.setLoginId("testuser");

        // 이미 동일한 아이디가 DB에 존재한다고 가정
        when(memberRepository.findByLoginId("testuser")).thenReturn(Optional.of(new Member()));

        // 가입 시도 시 IllegalArgumentException이 발생하는지 확인
        assertThrows(IllegalArgumentException.class, () -> authService.SignUp(request));
    }

    @Test
    @DisplayName("로그인_성공_테스트")
    void login_Success() {
        AuthLoginRequest request = new AuthLoginRequest();
        request.setLoginId("testuser");
        request.setMemberPassword("1234");

        Member mockMember = Member.builder()
                .loginId("testuser")
                .memberName("testuser")
                .password("encodedPassword")
                .build();
        mockMember.setMemberId(1L);

        when(memberRepository.findByLoginId("testuser")).thenReturn(Optional.of(mockMember));
        // 비밀번호 매칭이 성공한다고 가정
        when(passwordEncoder.matches("1234", "encodedPassword")).thenReturn(true);
        // 발급할 토큰 값 가정
        when(jwtTokenProvider.createToken("testuser")).thenReturn("mock-jwt-token");

        AuthLoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("테스터", response.getMemberName());
        assertEquals("mock-jwt-token", response.getAccessToken());
    }

    @Test
    @DisplayName("로그인_실패_비밀번호틀림")
    void login_Fail_WrongPassword() {
        AuthLoginRequest request = new AuthLoginRequest();
        request.setLoginId("testuser");
        request.setMemberPassword("wrongPassword");

        Member mockMember = Member.builder()
                .loginId("testuser")
                .password("encodedPassword")
                .build();

        when(memberRepository.findByLoginId("testuser")).thenReturn(Optional.of(mockMember));
        // 비밀번호 매칭이 실패한다고 가정
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.login(request));
        assertEquals("비밀번호 틀림", exception.getMessage());
    }
}