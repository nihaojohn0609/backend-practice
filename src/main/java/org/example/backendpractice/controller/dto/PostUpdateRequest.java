package org.example.backendpractice.controller.dto;

import lombok.Data;

@Data
public class PostUpdateRequest {
    private String title;
    private String content;
}
