package com.example.viewerLimiterSystem.controller;

import com.example.viewerLimiterSystem.dto.*;
import com.example.viewerLimiterSystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1/")
public class AuthController {

    final private AuthService authService;



    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/logout")
    public LoginResponse logout(@RequestBody LogoutRequest request) {
        return authService.logout(request);
    }

    @GetMapping("/sessions")
    public List<SessionResponse> getSessions(@RequestParam String email){
        return authService.getActiveSessions(email);
    }
}
