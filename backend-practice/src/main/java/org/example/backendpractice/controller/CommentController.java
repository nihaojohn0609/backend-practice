package org.example.backendpractice.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.CommentCreateRequest;
import org.example.backendpractice.controller.dto.CommentResponse;
import org.example.backendpractice.controller.dto.CommentUpdateRequest;
import org.example.backendpractice.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
@CrossOrigin("http://localhost:3000")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentResponse createComment(Authentication authentication, @PathVariable Long postId, @RequestBody CommentCreateRequest request) {
        String loginId = authentication.getName();
        return commentService.createComment(postId, loginId, request);
    }

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    @PutMapping("/{commentId}")
    public CommentResponse updateComment(Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        String loginId = authentication.getName();
        return commentService.UpdateComment(commentId, loginId, request);
    }

    @DeleteMapping("/{commentId}")
    void deleteComment(Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId) {
        String loginId = authentication.getName();
        commentService.deleteComment(commentId, loginId);
    }
}
