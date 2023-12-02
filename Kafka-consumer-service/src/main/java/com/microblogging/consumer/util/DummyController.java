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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class DummyController {

    @Autowired
    private EventHandlingService eventHandlingService;

    @GetMapping("/trigger/dummy")
    public String triggerEventHandling() throws JsonProcessingException {
        log.info("DummyController: triggerEventHandling() called.");
        // Create a mock TweetMessage to simulate an event
        List<String> likes = new ArrayList<>();
        likes.add("auth0|656a6930a19599c9209804bd");
        likes.add("auth0|656a6f382b2b0f327a499987");
        likes.add("auth0|656a695b34408e731c37b75e");


        TweetMessage mockTweetMessage = new TweetMessage(
                "tweet_created",
                "2023-10-17T14:30:00Z",
                "auth0|656a699c2b2b0f327a4993a2",
                "656a69ee78a7281da8a40381",
                "Hi, How's it going!! :)",
                "2023-12-01T23:21:05Z",
                likes
        );

        FollowEvent followEvent = new FollowEvent();
        followEvent.setEventType("tweet_updated");
        followEvent.setTimestamp(LocalDateTime.now());
        followEvent.setUserId("Charlie");
        followEvent.setFollowerId("Alice");

        // Call the event handling service with the mock message
       // eventHandlingService.handleTweetCreated(mockTweetMessage);
        //eventHandlingService.handleTweetDeleted(mockTweetMessage);
        eventHandlingService.handleTweetUpdated(mockTweetMessage);
        //eventHandlingService.handleUserFollowed(followEvent);
        //eventHandlingService.handleUserUnfollowed(followEvent);

        return "Event handling triggered.";
    }
}