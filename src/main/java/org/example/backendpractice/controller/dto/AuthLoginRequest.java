package org.example.backendpractice.controller.dto;

import lombok.Data;

@Data
public class AuthLoginRequest {
    private Long memberLoginId;
    private String memberPassword;
}
