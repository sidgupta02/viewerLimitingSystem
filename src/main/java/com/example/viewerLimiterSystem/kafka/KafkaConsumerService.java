package com.example.viewerLimiterSystem.kafka;

import com.example.viewerLimiterSystem.dto.KafkaSessionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(
            topics = "session-events",
            groupId = "viewer-group"
    )
    public void consume(KafkaSessionEvent event){
        System.out.println("EVENT RECEIVED");
        System.out.println("User: "+ event.getEmail());
        System.out.println("Event: "+ event.getEventType());
        System.out.println("Session: "+ event.getSessionId());
    }
}
