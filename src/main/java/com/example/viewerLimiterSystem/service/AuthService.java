package com.example.viewerLimiterSystem.service;

import com.example.viewerLimiterSystem.dto.*;
import com.example.viewerLimiterSystem.entity.Session;
import com.example.viewerLimiterSystem.entity.User;
import com.example.viewerLimiterSystem.enums.SessionStatus;
import com.example.viewerLimiterSystem.kafka.KafkaProducerService;
import com.example.viewerLimiterSystem.repository.SessionRepository;
import com.example.viewerLimiterSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    final private UserRepository userRepository;
    final private SessionRepository sessionRepository;
    final private ModelMapper modelMapper;
    final private RedisTemplate<String, Object> redisTemplate;
    final private SessionAsyncService sessionAsyncService;
    private final KafkaProducerService kafkaProducerService;

    public LoginResponse login(LoginRequest loginRequest){

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));

        if(!user.getPassword().equals(loginRequest.getPassword())){
            return new LoginResponse("Invalid Password", false,null);
        }

        String key = "user:" + user.getEmail() + ":sessions";
        Long activeSession = redisTemplate.opsForSet().size(key);
        //long activeSession = sessionRepository
        //        .countByUserAndStatusAndExpiresAtAfter(user, SessionStatus.ACTIVE, LocalDateTime.now());

        if(activeSession != null && activeSession >= user.getMaxDevices()){
            return new LoginResponse("Device Limit reached", false,null);
        }

        Session session = new Session();
        String sessionId = UUID.randomUUID().toString();
        //save session id in reddis
        redisTemplate.opsForSet().add(key, sessionId);
        // ttl for session keys
        redisTemplate.expire(key, Duration.ofMinutes(30));

        session.setUser(user);
        session.setSessionId(sessionId);
        session.setDeviceInfo(loginRequest.getDeviceInfo());
        session.setStatus(SessionStatus.ACTIVE);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        sessionAsyncService.saveSession(session);

        KafkaSessionEvent event = new KafkaSessionEvent(
                user.getEmail(),
                session.getSessionId(),
                "LOGIN",
                session.getDeviceInfo()
        );
        kafkaProducerService.sendSessionEvent(event);
        //sessionRepository.save(session);
        return new LoginResponse("Login Successful", true,session.getSessionId());
    }

    public LoginResponse register(RegisterRequest registerRequest){
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            return new LoginResponse("User already exist", false,null);
        }

        User user = modelMapper.map(registerRequest, User.class);
        userRepository.save(user);

        return new LoginResponse("User created successfully", true,null);
    }

    public LoginResponse logout(LogoutRequest request){
        Session session = sessionRepository
                .findBySessionId(request.getSessionId())
                .orElseThrow(()-> new RuntimeException("Session not found"));

        if(session.getExpiresAt().isBefore(LocalDateTime.now())){
            return new LoginResponse("Session already expired", false, session.getSessionId());
        }

        if(session.getStatus()==SessionStatus.INACTIVE){
            return new LoginResponse("Session already logged out", false,session.getSessionId());
        }

        //redis remove
        String key = "user:" + session.getUser().getEmail() + ":sessions";
        redisTemplate.opsForSet().remove(key, request.getSessionId());

        session.setStatus(SessionStatus.INACTIVE);
        session.setLoggedOutAt(LocalDateTime.now());
        sessionAsyncService.saveSession(session);

        KafkaSessionEvent event = new KafkaSessionEvent(
                session.getUser().getEmail(),
                session.getSessionId(),
                "LOGOUT",
                session.getDeviceInfo()
        );
        kafkaProducerService.sendSessionEvent(event);
        //sessionRepository.save(session);

        return new LoginResponse("Session logged out successfully", true, session.getSessionId());
    }

    public List<SessionResponse> getActiveSessions(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        List<Session> sessions = sessionRepository.findByUserAndStatusAndExpiresAtAfter(
                user,
                SessionStatus.ACTIVE,
                LocalDateTime.now()
        );

        return sessions.stream()
                .map(s -> new SessionResponse(
                        s.getSessionId(),
                        s.getDeviceInfo(),
                        s.getCreatedAt()
                ))
                .toList();
    }
}
