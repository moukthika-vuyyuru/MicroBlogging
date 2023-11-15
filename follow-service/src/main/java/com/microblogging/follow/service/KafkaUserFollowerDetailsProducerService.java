package com.microblogging.follow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microblogging.follow.model.KafkaFollowMessageDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaUserFollowerDetailsProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    public KafkaUserFollowerDetailsProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(KafkaFollowMessageDetails kafkaFollowMessageDetails) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var json = objectMapper.writeValueAsString(kafkaFollowMessageDetails);
        System.out.println("Message"+ json);
        kafkaTemplate.send("tweet-updates-topic", json)
                .addCallback(
                        result -> log.info("Message sent successfully to topic: {}", result.getRecordMetadata().topic()),
                        ex -> log.error("Failed to send message", ex)
                );

    }

}
