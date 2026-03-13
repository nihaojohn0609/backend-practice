package org.example.backendpractice.controller.dto;

import lombok.Data;

@Data
public class PostCreateRequest {
    // 작성자는 클라이언트가 보내는 값이 아니라, 헤더에 담긴 JWT 토큰에서 서버가 직접 꺼내서 안전하게 사용
    private String title;
    private String content;
}
