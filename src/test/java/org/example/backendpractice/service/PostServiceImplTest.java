package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.PostCreateRequest;
import org.example.backendpractice.controller.dto.PostPageResponse;
import org.example.backendpractice.controller.dto.PostResponse;
import org.example.backendpractice.controller.dto.PostUpdateRequest;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.dao.PostRepository;
import org.example.backendpractice.entity.Member;
import org.example.backendpractice.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PostServiceImpl postService;

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

    @Test
    @DisplayName("게시글_생성_성공")
    void createPost_Success() {
        Member member = createMockMember();
        when(memberRepository.findByLoginId("testuser")).thenReturn(Optional.of(member));

        Post savedPost = Post.builder()
                .title("새 글")
                .content("새 내용")
                .member(member)
                .build();
        savedPost.setPostId(1L);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("새 글");
        request.setContent("새 내용");

        PostResponse response = postService.createPost("testuser", request);

        assertNotNull(response);
        assertEquals("새 글", response.getTitle());
        assertEquals("새 내용", response.getContent());
        assertEquals("테스터", response.getWriterName());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글_생성_실패_존재하지않는회원")
    void createPost_Fail_MemberNotFound() {
        when(memberRepository.findByLoginId("unknown")).thenReturn(Optional.empty());

        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("제목");
        request.setContent("내용");

        assertThrows(IllegalArgumentException.class,
                () -> postService.createPost("unknown", request));
    }

    @Test
    @DisplayName("전체_게시글_조회_성공")
    void findAllPosts_Success() {
        Member member = createMockMember();
        Post post1 = createMockPost(member);
        Post post2 = Post.builder()
                .title("두번째 글")
                .content("두번째 내용")
                .member(member)
                .build();
        post2.setPostId(2L);

        Page<Post> mockPage = new PageImpl<>(List.of(post1, post2));
        when(postRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        PostPageResponse result = postService.findAllPosts(0, 10, "latest");

        assertEquals(2, result.getPosts().size());
        assertEquals("테스트 제목", result.getPosts().get(0).getTitle());
        assertEquals("두번째 글", result.getPosts().get(1).getTitle());
        assertEquals(0, result.getCurrentPage());
        assertEquals(10, result.getTotalPages());
        assertFalse(result.isHasNext());
    }

    @Test
    @DisplayName("게시글_단건_조회_성공")
    void findPostById_Success() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostResponse response = postService.findPostById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getPostId());
        assertEquals("테스트 제목", response.getTitle());
        assertEquals("테스터", response.getWriterName());
    }

    @Test
    @DisplayName("게시글_조회_실패_존재하지않는게시글")
    void findPostById_Fail_NotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postService.findPostById(99L));
        assertEquals("존재하지 않는 게시글", exception.getMessage());
    }

    @Test
    @DisplayName("게시글_수정_성공")
    void updatePost_Success() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostUpdateRequest request = new PostUpdateRequest();
        request.setTitle("수정된 제목");
        request.setContent("수정된 내용");

        PostResponse response = postService.updatePost("testuser", 1L, request);

        assertEquals("수정된 제목", response.getTitle());
        assertEquals("수정된 내용", response.getContent());
    }

    @Test
    @DisplayName("게시글_수정_실패_권한없음")
    void updatePost_Fail_Unauthorized() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostUpdateRequest request = new PostUpdateRequest();
        request.setTitle("해킹");
        request.setContent("해킹");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postService.updatePost("otheruser", 1L, request));
        assertEquals("수정 권한 없음", exception.getMessage());
    }

    @Test
    @DisplayName("게시글_수정_실패_존재하지않는게시글")
    void updatePost_Fail_NotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        PostUpdateRequest request = new PostUpdateRequest();

        assertThrows(IllegalArgumentException.class,
                () -> postService.updatePost("testuser", 99L, request));
    }

    @Test
    @DisplayName("게시글_삭제_성공")
    void deletePost_Success() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost("testuser", 1L);

        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("게시글_삭제_실패_권한없음")
    void deletePost_Fail_Unauthorized() {
        Member member = createMockMember();
        Post post = createMockPost(member);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postService.deletePost("otheruser", 1L));
        assertEquals("삭제 권한 없음", exception.getMessage());
    }

    @Test
    @DisplayName("게시글_삭제_실패_존재하지않는게시글")
    void deletePost_Fail_NotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> postService.deletePost("testuser", 99L));
    }

    @Test
    @DisplayName("탈퇴한_사용자_게시글_조회")
    void findPost_DeletedMember() {
        Post post = Post.builder()
                .title("탈퇴자 글")
                .content("내용")
                .member(null)
                .build();
        post.setPostId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostResponse response = postService.findPostById(1L);

        assertEquals("(알수없음) 탈퇴한 사용자", response.getWriterName());
    }
}
