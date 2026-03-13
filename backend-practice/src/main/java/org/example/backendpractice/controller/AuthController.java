package org.example.backendpractice.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.AuthLoginRequest;
import org.example.backendpractice.controller.dto.AuthLoginResponse;
import org.example.backendpractice.controller.dto.AuthSignUpRequest;
import org.example.backendpractice.service.AuthService;
import org.example.backendpractice.service.AuthServiceImpl;
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
}
