package com.example.viewerLimiterSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaSessionEvent {
    private String email;
    private String sessionId;
    private String eventType;
    private String deviceInfo;
}

