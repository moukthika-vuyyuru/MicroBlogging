package com.microblogging.consumer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microblogging.consumer.handler.EventHandlingService;
import com.microblogging.consumer.model.FollowEvent;
import com.microblogging.consumer.model.TweetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class DummyController {

    @Autowired
    private EventHandlingService eventHandlingService;

    @GetMapping("/trigger/dummy")
    public String triggerEventHandling() throws JsonProcessingException {
        log.info("DummyController: triggerEventHandling() called.");
        // Create a mock TweetMessage to simulate an event

        TweetMessage mockTweetMessage = new TweetMessage(
                "tweet_created",
                "2023-10-17T14:30:00Z",
                "Alice",
                "tweet567",
                "BB",
                null
        );

        FollowEvent followEvent = new FollowEvent();
        followEvent.setEventType("user_unfollowed");
        followEvent.setTimestamp(LocalDateTime.now());
        followEvent.setUserId("Charlie");
        followEvent.setFollowerId("Alice");

        // Call the event handling service with the mock message
       // eventHandlingService.handleTweetCreated(mockTweetMessage);
        //eventHandlingService.handleTweetDeleted(mockTweetMessage);
        //eventHandlingService.handleTweetUpdated(mockTweetMessage);
        //eventHandlingService.handleUserFollowed(followEvent);
        eventHandlingService.handleUserUnfollowed(followEvent);

        return "Event handling triggered.";
    }
}