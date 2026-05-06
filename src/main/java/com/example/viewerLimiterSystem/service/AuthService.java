package com.example.viewerLimiterSystem.service;

import com.example.viewerLimiterSystem.dto.LoginRequest;
import com.example.viewerLimiterSystem.dto.LoginResponse;
import com.example.viewerLimiterSystem.dto.LogoutRequest;
import com.example.viewerLimiterSystem.dto.RegisterRequest;
import com.example.viewerLimiterSystem.entity.Session;
import com.example.viewerLimiterSystem.entity.User;
import com.example.viewerLimiterSystem.enums.SessionStatus;
import com.example.viewerLimiterSystem.repository.SessionRepository;
import com.example.viewerLimiterSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    final private UserRepository userRepository;
    final private SessionRepository sessionRepository;
    final private ModelMapper modelMapper;

    public LoginResponse login(LoginRequest loginRequest){

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));

        if(!user.getPassword().equals(loginRequest.getPassword())){
            return new LoginResponse("Invalid Password", false,null);
        }

        long activeSession = sessionRepository
                .countByUserAndStatusAndExpiresAtAfter(user, SessionStatus.ACTIVE, LocalDateTime.now());

        if(activeSession >= user.getMaxDevices()){
            return new LoginResponse("Device Limit reached", false,null);
        }

        Session session = new Session();
        session.setUser(user);
        session.setSessionId(UUID.randomUUID().toString());
        session.setDeviceInfo(loginRequest.getDeviceInfo());
        session.setStatus(SessionStatus.ACTIVE);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        sessionRepository.save(session);
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
        session.setStatus(SessionStatus.INACTIVE);
        session.setLoggedOutAt(LocalDateTime.now());
        sessionRepository.save(session);

        return new LoginResponse("Session logged out successfully", true, session.getSessionId());
    }
}
