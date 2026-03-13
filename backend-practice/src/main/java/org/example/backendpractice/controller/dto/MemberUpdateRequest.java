package org.example.backendpractice.controller.dto;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private String memberName;
    private String password;
}
