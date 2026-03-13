package org.example.backendpractice.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.example.backendpractice.entity.Comment;

@Data
@Builder
public class CommentResponse {
    private Long commentId;
    private String content;
    private String author;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .author(comment.getMember() != null ? comment.getMember().getMemberName() : "탈퇴한 사용자")
                .build();
    }
}
