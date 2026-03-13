package org.example.backendpractice.controller.dto;

import lombok.Data;

@Data
public class AuthSignUpRequest {
    private String loginId;
    private String memberName;
    private String memberPassword;
}
