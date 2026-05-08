package com.example.viewerLimiterSystem.service;

import com.example.viewerLimiterSystem.entity.Session;
import com.example.viewerLimiterSystem.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionAsyncService {

    private final SessionRepository sessionRepository;

    @Async
    public void saveSession(Session session){
        System.out.println("Saving session in async thread: "+ Thread.currentThread().getName());

        sessionRepository.save(session);
    }
}
