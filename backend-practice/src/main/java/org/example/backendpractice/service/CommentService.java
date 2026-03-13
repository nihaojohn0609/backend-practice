package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.CommentCreateRequest;
import org.example.backendpractice.controller.dto.CommentResponse;
import org.example.backendpractice.controller.dto.CommentUpdateRequest;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(Long postId, String loginId, CommentCreateRequest request);

    List<CommentResponse> getCommentsByPost(Long postId);

    CommentResponse UpdateComment(Long commentId, String loginId, CommentUpdateRequest request);

    void deleteComment(Long commentId, String loginId);
}