package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.AuthLoginRequest;
import org.example.backendpractice.controller.dto.AuthLoginResponse;
import org.example.backendpractice.controller.dto.AuthSignUpRequest;

public interface AuthService {

    void validateLoginId(String loginId);

    void SignUp(AuthSignUpRequest request);

    AuthLoginResponse login(AuthLoginRequest request);
}
