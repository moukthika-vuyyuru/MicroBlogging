package com.microblogging.consumer.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.microblogging.consumer.handler.EventHandlingService;
import com.microblogging.consumer.model.TweetMessage;
import com.microblogging.consumer.model.FollowEvent;
import com.microblogging.consumer.util.LocalDateTimeDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventHandlingService eventHandlingService;

    public KafkaConsumer() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        objectMapper.registerModule(module);
    }

    @KafkaListener(topics = "tweet-updates-topic", groupId = "kafka-consumer-group-1")
    public void consume(String message) throws JsonProcessingException {
        log.info("Received message in group kafka-consumer-group-1: " + message);
        JsonNode jsonNode = objectMapper.readTree(message);
        String eventType = jsonNode.get("eventType").asText();

        try {
            switch (eventType) {
                case "tweet_created":
                case "tweet_updated":
                case "tweet_deleted":
                    TweetMessage tweetMessage = objectMapper.treeToValue(jsonNode, TweetMessage.class);
                    handleTweetEvent(tweetMessage);
                    break;
                case "user_followed":
                case "user_unfollowed":
                    FollowEvent followEvent = objectMapper.treeToValue(jsonNode, FollowEvent.class);
                    handleFollowEvent(followEvent);
                    break;
                default:
                    log.warn("Unknown event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Error processing message", e);
            // Handle the exception appropriately
        }
        log.info("Event handling completed for message: " + message);
    }

    private void handleTweetEvent(TweetMessage tweetMessage) {
        log.info("Deserialized tweet event: " + tweetMessage);
        switch (tweetMessage.getEventType()) {
            case "tweet_created":
                eventHandlingService.handleTweetCreated(tweetMessage);
                break;
            case "tweet_updated":
                eventHandlingService.handleTweetUpdated(tweetMessage);
                break;
            case "tweet_deleted":
                eventHandlingService.handleTweetDeleted(tweetMessage);
                break;
        }
    }

    private void handleFollowEvent(FollowEvent followEvent) {
        log.info("Deserialized follow event: " + followEvent);
        switch (followEvent.getEventType()) {
            case "user_followed":
                eventHandlingService.handleUserFollowed(followEvent);
                break;
            case "user_unfollowed":
                eventHandlingService.handleUserUnfollowed(followEvent);
                break;
        }
    }
}
