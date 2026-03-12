package org.example.backendpractice.service;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.PostCreateRequest;
import org.example.backendpractice.controller.dto.PostResponse;
import org.example.backendpractice.controller.dto.PostUpdateRequest;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.dao.PostRepository;
import org.example.backendpractice.entity.Member;
import org.example.backendpractice.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public PostResponse createPost(String loginId, PostCreateRequest request) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .build();

        Post createdPost = postRepository.save(post);
        return PostResponse.from(createdPost);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : posts) {
            postResponses.add(PostResponse.from(post));
        }
        return postResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        return PostResponse.from(post);
    }

    @Override
    @Transactional
    public PostResponse updatePost(String loginId, Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));

        if(!post.getMember().getLoginId().equals(loginId)) {
            throw new IllegalArgumentException("수정 권한 없음");
        }

        post.updatePost(request.getTitle(), request.getContent());
        postRepository.save(post);
        return PostResponse.from(post);
    }

    @Override
    public void deletePost(String loginId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));

        if(!post.getMember().getLoginId().equals(loginId)) {
            throw new IllegalArgumentException("삭제 권한 없음");
        }

        postRepository.delete(post);
    }
}
