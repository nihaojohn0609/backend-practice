package org.example.backendpractice.controller.dto;

import lombok.Data;

@Data
public class AuthPasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
}
