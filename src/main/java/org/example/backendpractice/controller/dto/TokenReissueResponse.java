package org.example.backendpractice.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenReissueResponse {
    private String accessToken;
    private String refreshToken;
}
