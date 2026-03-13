package org.example.backendpractice.controller.dto;

import lombok.Data;

@Data
public class AuthLoginRequest {
    private String loginId;
    private String memberPassword;
}
