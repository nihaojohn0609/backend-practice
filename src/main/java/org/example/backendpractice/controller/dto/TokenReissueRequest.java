package org.example.backendpractice.controller.dto;

import lombok.Data;

@Data
public class TokenReissueRequest {
    private String refreshToken;
}
