package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.MemberResponse;
import org.example.backendpractice.controller.dto.MemberUpdateRequest;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member createMockMember() {
        Member member = Member.builder()
                .loginId("testuser")
                .memberName("테스터")
                .password("encodedPassword")
                .build();
        member.setMemberId(1L);
        return member;
    }

    @Test
    @DisplayName("전체_회원_조회_성공")
    void findAllMembers_Success() {
        Member member1 = createMockMember();
        Member member2 = Member.builder()
                .loginId("user2")
                .memberName("유저2")
                .password("encoded2")
                .build();
        member2.setMemberId(2L);

        when(memberRepository.findAll()).thenReturn(List.of(member1, member2));

        List<MemberResponse> result = memberService.findAllMembers();

        assertEquals(2, result.size());
        assertEquals("테스터", result.get(0).getMemberName());
        assertEquals("유저2", result.get(1).getMemberName());
    }

    @Test
    @DisplayName("회원_단건_조회_성공")
    void findById_Success() {
        Member member = createMockMember();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberResponse response = memberService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getMemberId());
        assertEquals("테스터", response.getMemberName());
    }

    @Test
    @DisplayName("회원_조회_실패_존재하지않는회원")
    void findById_Fail_NotFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> memberService.findById(99L));
        assertEquals("존재하지 않는 회원입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("회원_정보_수정_성공_이름만변경")
    void updateMember_Success_NameOnly() {
        Member member = createMockMember();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setMemberName("새이름");
        request.setPassword("");

        MemberResponse response = memberService.updateMember(1L, "testuser", request);

        assertEquals("새이름", response.getMemberName());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    @DisplayName("회원_정보_수정_성공_비밀번호변경")
    void updateMember_Success_WithPassword() {
        Member member = createMockMember();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");

        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setMemberName("새이름");
        request.setPassword("newpass");

        MemberResponse response = memberService.updateMember(1L, "testuser", request);

        assertEquals("새이름", response.getMemberName());
        verify(passwordEncoder, times(1)).encode("newpass");
    }

    @Test
    @DisplayName("회원_정보_수정_실패_권한없음")
    void updateMember_Fail_Unauthorized() {
        Member member = createMockMember();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setMemberName("해커");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> memberService.updateMember(1L, "otheruser", request));
        assertEquals("수정 권한 없음", exception.getMessage());
    }

    @Test
    @DisplayName("회원_정보_수정_실패_존재하지않는회원")
    void updateMember_Fail_NotFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        MemberUpdateRequest request = new MemberUpdateRequest();

        assertThrows(IllegalArgumentException.class,
                () -> memberService.updateMember(99L, "testuser", request));
    }

    @Test
    @DisplayName("회원_삭제_성공")
    void deleteMember_Success() {
        Member member = createMockMember();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        memberService.deleteMember(1L, "testuser");

        verify(memberRepository, times(1)).delete(member);
    }

    @Test
    @DisplayName("회원_삭제_실패_권한없음")
    void deleteMember_Fail_Unauthorized() {
        Member member = createMockMember();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> memberService.deleteMember(1L, "otheruser"));
        assertEquals("수정 권한 없음", exception.getMessage());
    }

    @Test
    @DisplayName("회원_삭제_실패_존재하지않는회원")
    void deleteMember_Fail_NotFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> memberService.deleteMember(99L, "testuser"));
    }
}
