package com.example.viewerLimiterSystem.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Integer maxDevices;
}
