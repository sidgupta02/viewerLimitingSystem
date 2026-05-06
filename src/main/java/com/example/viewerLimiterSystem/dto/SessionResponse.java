package com.example.viewerLimiterSystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionResponse {

    private String sessionId;
    private String deviceInfo;
    private LocalDateTime createdAt;

    public SessionResponse(String sessionId, String deviceInfo, LocalDateTime createdAt) {
        this.sessionId = sessionId;
        this.deviceInfo = deviceInfo;
        this.createdAt = createdAt;
    }
}
