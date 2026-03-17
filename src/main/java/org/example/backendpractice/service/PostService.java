package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.PostCreateRequest;
import org.example.backendpractice.controller.dto.PostPageResponse;
import org.example.backendpractice.controller.dto.PostResponse;
import org.example.backendpractice.controller.dto.PostUpdateRequest;

import java.util.List;

public interface PostService {
    PostResponse createPost(String loginId, PostCreateRequest request);
    PostPageResponse findAllPosts(int page, int size, String sortBy);
    PostResponse findPostById(Long postId);
    PostResponse updatePost(String loginId, Long postId, PostUpdateRequest request);
    void deletePost(String loginId, Long postId);
}
