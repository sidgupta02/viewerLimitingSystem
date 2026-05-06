package com.example.viewerLimiterSystem.entity;

import com.example.viewerLimiterSystem.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;
    private String deviceInfo;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime loggedOutAt;
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
