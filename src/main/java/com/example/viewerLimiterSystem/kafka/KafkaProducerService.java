package com.example.viewerLimiterSystem.kafka;

import com.example.viewerLimiterSystem.dto.KafkaSessionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "session-events";

    public void sendSessionEvent(KafkaSessionEvent event){
        kafkaTemplate.send(TOPIC, event);
    }
}
