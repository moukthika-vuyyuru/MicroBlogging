package com.microblogging.feed.controller;

import com.microblogging.feed.model.UserTimeline;
import com.microblogging.feed.model.UserTimelineKey;
import com.microblogging.feed.service.FeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FeedControllerTest {

    @Mock
    private FeedService feedService;

    @InjectMocks
    private FeedController feedController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFeedForUserSuccess() {
        String userId = "testUser";
        int limit = 1;
        UserTimeline userTimeline = new UserTimeline();
        UserTimelineKey userTimelineKey = new UserTimelineKey();
        userTimelineKey.setUserId(userId);
        userTimelineKey.setTweetTimestamp(LocalDateTime.now());
        userTimeline.setKey(userTimelineKey);
        userTimeline.setAuthor_id("user2");

        when(feedService.getUserTimeline(userId, limit, null)).thenReturn(Arrays.asList(userTimeline));

        ResponseEntity<?> response = feedController.getFeedForUser(userId, limit, null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetFeedForUserFailure() {
        String userId = "testUser";
        int limit = 1;
        when(feedService.getUserTimeline(anyString(), anyInt(), any())).thenThrow(RuntimeException.class);

        ResponseEntity<?> response = feedController.getFeedForUser(userId, limit, null);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
