package com.example.viewerLimiterSystem.controller;

import com.example.viewerLimiterSystem.dto.LoginRequest;
import com.example.viewerLimiterSystem.dto.LoginResponse;
import com.example.viewerLimiterSystem.dto.LogoutRequest;
import com.example.viewerLimiterSystem.dto.RegisterRequest;
import com.example.viewerLimiterSystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
