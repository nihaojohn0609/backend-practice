package org.example.backendpractice.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.example.backendpractice.entity.Member;

@Data
@Builder
public class AuthLoginResponse {
    private Long memberId;
    private String memberName;
    private String accessToken;

    public static AuthLoginResponse from(Member member, String accessToken) {
        return AuthLoginResponse.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .accessToken(accessToken)
                .build();
    }
}
