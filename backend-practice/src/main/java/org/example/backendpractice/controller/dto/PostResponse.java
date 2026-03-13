package org.example.backendpractice.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.example.backendpractice.entity.Post;

@Data
@Builder
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private String writerName;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                // user가 null이면 "탈퇴한 사용자", 아니면 원래 이름 표시
                .writerName(post.getMember() != null ? post.getMember().getMemberName() : "(알수없음) 탈퇴한 사용자")
                .build();
    }
}
