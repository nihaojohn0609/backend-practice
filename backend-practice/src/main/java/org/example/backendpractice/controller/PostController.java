package org.example.backendpractice.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.PostCreateRequest;
import org.example.backendpractice.controller.dto.PostResponse;
import org.example.backendpractice.controller.dto.PostUpdateRequest;
import org.example.backendpractice.dao.PostRepository;
import org.example.backendpractice.entity.Post;
import org.example.backendpractice.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    private final PostService postService;

    // 클라이언트가 헤더에 JWT 토큰을 담아 보내면, JwtAuthenticationFilter가 토큰을 해독해서 유저 아이디를 꺼낸 뒤, Authentication 객체 안에 몰래 넣어줍니다.

    @PostMapping
    public PostResponse createPost(Authentication authentication, @RequestBody PostCreateRequest request) {
        String loginId = authentication.getName(); // JWT 토큰 안에 있던 아이디 꺼내기
        return postService.createPost(loginId, request);
    }

    @GetMapping
    public List<PostResponse> getPosts() {
        return postService.findAllPosts();
    }

    @GetMapping("/{postId}")
    public PostResponse findPostById(@PathVariable Long postId) {
        return postService.findPostById(postId);
    }

    @PutMapping("/{postId}")
    public PostResponse updatePost(Authentication authentication, @PathVariable Long postId, @RequestBody PostUpdateRequest request) {
        String loginId = authentication.getName();
        return postService.updatePost(loginId, postId, request);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(Authentication authentication, @PathVariable Long postId) {
        String loginId = authentication.getName();
        postService.deletePost(loginId, postId);
    }
}
