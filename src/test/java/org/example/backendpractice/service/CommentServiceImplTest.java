package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.CommentCreateRequest;
import org.example.backendpractice.controller.dto.CommentResponse;
import org.example.backendpractice.controller.dto.CommentUpdateRequest;
import org.example.backendpractice.dao.CommentRepository;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.dao.PostRepository;
import org.example.backendpractice.entity.Comment;
import org.example.backendpractice.entity.Member;
import org.example.backendpractice.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Member createMockMember() {
        Member member = Member.builder()
                .loginId("testuser")
                .memberName("테스터")
                .password("encodedPassword")
                .build();
        member.setMemberId(1L);
        return member;
    }

    private Post createMockPost(Member member) {
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .member(member)
                .build();
        post.setPostId(1L);
        return post;
    }

    private Comment createMockComment(Post post, Member member) {
        Comment comment = Comment.builder()
                .content("테스트 댓글")
                .post(post)
                .member(member)
                .build();
        comment.setCommentId(1L);
        return comment;
    }

    @Test
    @DisplayName("댓글_생성_성공")
    void createComment_Success() {
        Member member = createMockMember();
        Post post = createMockPost(member);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(memberRepository.findByLoginId("testuser")).thenReturn(Optional.of(member));

        Comment savedComment = createMockComment(post, member);
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("테스트 댓글");

        CommentResponse response = commentService.createComment(1L, "testuser", request);

        assertNotNull(response);
        assertEquals("테스트 댓글", response.getContent());
        assertEquals("테스터", response.getAuthor());
    }

    @Test
    @DisplayName("댓글_생성_실패_존재하지않는게시글")
    void createComment_Fail_PostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("댓글");

        assertThrows(IllegalArgumentException.class,
                () -> commentService.createComment(99L, "testuser", request));
    }

    @Test
    @DisplayName("댓글_생성_실패_존재하지않는회원")
    void createComment_Fail_MemberNotFound() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(memberRepository.findByLoginId("unknown")).thenReturn(Optional.empty());

        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("댓글");

        assertThrows(IllegalArgumentException.class,
                () -> commentService.createComment(1L, "unknown", request));
    }

    @Test
    @DisplayName("게시글별_댓글_조회_성공")
    void getCommentsByPost_Success() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        Comment comment1 = createMockComment(post, member);
        Comment comment2 = Comment.builder().content("두번째 댓글").post(post).member(member).build();
        comment2.setCommentId(2L);

        when(commentRepository.findByPost_PostId(1L)).thenReturn(List.of(comment1, comment2));

        List<CommentResponse> result = commentService.getCommentsByPost(1L);

        assertEquals(2, result.size());
        assertEquals("테스트 댓글", result.get(0).getContent());
        assertEquals("두번째 댓글", result.get(1).getContent());
    }

    @Test
    @DisplayName("댓글_수정_성공")
    void updateComment_Success() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        Comment comment = createMockComment(post, member);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentUpdateRequest request = new CommentUpdateRequest();
        request.setContent("수정된 댓글");

        CommentResponse response = commentService.UpdateComment(1L, "testuser", request);

        assertEquals("수정된 댓글", response.getContent());
    }

    @Test
    @DisplayName("댓글_수정_실패_권한없음")
    void updateComment_Fail_Unauthorized() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        Comment comment = createMockComment(post, member);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentUpdateRequest request = new CommentUpdateRequest();
        request.setContent("해킹");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> commentService.UpdateComment(1L, "otheruser", request));
        assertEquals("권한 없음", exception.getMessage());
    }

    @Test
    @DisplayName("댓글_수정_실패_존재하지않는댓글")
    void updateComment_Fail_NotFound() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        CommentUpdateRequest request = new CommentUpdateRequest();
        request.setContent("수정");

        assertThrows(IllegalArgumentException.class,
                () -> commentService.UpdateComment(99L, "testuser", request));
    }

    @Test
    @DisplayName("댓글_삭제_성공")
    void deleteComment_Success() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        Comment comment = createMockComment(post, member);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L, "testuser");

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    @DisplayName("댓글_삭제_실패_권한없음")
    void deleteComment_Fail_Unauthorized() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        Comment comment = createMockComment(post, member);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> commentService.deleteComment(1L, "otheruser"));
        assertEquals("권한 없음", exception.getMessage());
    }

    @Test
    @DisplayName("댓글_삭제_실패_존재하지않는댓글")
    void deleteComment_Fail_NotFound() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> commentService.deleteComment(99L, "testuser"));
    }

    @Test
    @DisplayName("탈퇴한_사용자_댓글_표시")
    void comment_DeletedMember() {
        Post post = createMockPost(createMockMember());
        Comment comment = Comment.builder().content("탈퇴자 댓글").post(post).member(null).build();
        comment.setCommentId(1L);

        when(commentRepository.findByPost_PostId(1L)).thenReturn(List.of(comment));

        List<CommentResponse> result = commentService.getCommentsByPost(1L);

        assertEquals("탈퇴한 사용자", result.get(0).getAuthor());
    }
}
