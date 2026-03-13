package org.example.backendpractice.service;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.CommentCreateRequest;
import org.example.backendpractice.controller.dto.CommentResponse;
import org.example.backendpractice.controller.dto.CommentUpdateRequest;
import org.example.backendpractice.dao.CommentRepository;
import org.example.backendpractice.dao.MemberRepository;
import org.example.backendpractice.dao.PostRepository;
import org.example.backendpractice.entity.Comment;
import org.example.backendpractice.entity.Member;
import org.example.backendpractice.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public CommentResponse createComment(Long postId, String loginId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .member(member)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(Long postId) {
        return commentRepository.findByPost_PostId(postId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponse UpdateComment(Long commentId, String loginId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글"));
        if(comment.getMember() == null || !comment.getMember().getLoginId().equals(loginId)) {
            throw new IllegalArgumentException("권한 없음");
        }

        comment.updateComment(request.getContent());
        return CommentResponse.from(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, String loginId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글"));
        if(comment.getMember() == null || !comment.getMember().getLoginId().equals(loginId)) {
            throw new IllegalArgumentException("권한 없음");
        }
        commentRepository.delete(comment);
    }
}
