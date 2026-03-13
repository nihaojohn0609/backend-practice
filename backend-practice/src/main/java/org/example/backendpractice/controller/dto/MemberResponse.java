package org.example.backendpractice.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.example.backendpractice.entity.Member;

@Data
@Builder
public class MemberResponse {
    private Long memberId;
    private String memberName;
    private String password;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .password(member.getPassword())
                .build();
    }
}
