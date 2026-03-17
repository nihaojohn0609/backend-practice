package org.example.backendpractice.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.*;
import org.example.backendpractice.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/members/validate")
    public void validateLoginId(@RequestParam String loginId) {
        authService.validateLoginId(loginId);
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody AuthSignUpRequest request) {
        authService.SignUp(request);
    }

    @PostMapping("/login")
    public AuthLoginResponse login(@RequestBody AuthLoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/reissue")
    public TokenReissueResponse reissue(@RequestBody TokenReissueRequest request) {
        return authService.reissue(request);
    }
}