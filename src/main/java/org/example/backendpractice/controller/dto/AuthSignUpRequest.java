package org.example.backendpractice.controller.dto;

import lombok.Data;

@Data
public class AuthSignUpRequest {
    private String memberName;
    private Long memberId;
    private String memberPassword;
}
