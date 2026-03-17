package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.*;

public interface AuthService {

    void validateLoginId(String loginId);

    void SignUp(AuthSignUpRequest request);

    AuthLoginResponse login(AuthLoginRequest request);

    TokenReissueResponse reissue(TokenReissueRequest request);
}
