package com.example.viewerLimiterSystem.repository;

import com.example.viewerLimiterSystem.entity.Session;
import com.example.viewerLimiterSystem.entity.User;
import com.example.viewerLimiterSystem.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    long countByUserAndStatus(User user, SessionStatus status);

    long countByUserAndStatusAndExpiresAtAfter(User user, SessionStatus status, LocalDateTime time);

    Optional<Session> findBySessionId(String sessionId);

    List<Session> findByUserAndStatusAndExpiresAtAfter(User user, SessionStatus status, LocalDateTime time);
}
