package com.microblogging.consumer.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microblogging.consumer.model.FollowEvent;
import com.microblogging.consumer.model.TweetMessage;
import com.microblogging.consumer.model.UserTimeline;
import com.microblogging.consumer.model.UserTimelineKey;
import com.microblogging.consumer.repo.FollowerRepository;
import com.microblogging.consumer.repo.UserTimelineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

class EventHandlingServiceImplTest {

    @Mock
    private UserTimelineRepository userTimelineRepository;

    @Mock
    private FollowerRepository followerRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EventHandlingServiceImpl eventHandlingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleTweetCreated() {
        TweetMessage tweetMessage = new TweetMessage();
        tweetMessage.setUserId("user123");
        tweetMessage.setTweetId("tweet123");
        tweetMessage.setTimestamp(String.valueOf(LocalDateTime.now()));
        tweetMessage.setContent("Sample tweet content");

        Set<String> followerIds = new HashSet<>(Arrays.asList("follower1", "follower2"));
        when(followerRepository.getFollowers("user123")).thenReturn(followerIds);

        LocalDateTime convertedDate = LocalDateTime.now();
        when(objectMapper.convertValue(tweetMessage.getTimestamp(), LocalDateTime.class)).thenReturn(convertedDate);

        eventHandlingService.handleTweetCreated(tweetMessage);

        verify(followerRepository, times(1)).getFollowers("user123");
        verify(userTimelineRepository, times(followerIds.size())).save(any(UserTimeline.class));
    }

    @Test
    void handleUserUnfollowed() {
        FollowEvent followEvent = new FollowEvent();
        followEvent.setUserId("user123");
        followEvent.setFollowerId("follower1");
        followEvent.setEventType("user_unfollowed");

        UserTimeline userTimeline = new UserTimeline();
        userTimeline.setKey(new UserTimelineKey("follower1", LocalDateTime.now(), "tweet123"));
        userTimeline.setContent("Sample tweet content");
        userTimeline.setAuthor_id("user123");

        List<UserTimeline> userTimelines = Arrays.asList(userTimeline); // Mocked UserTimeline list
        when(userTimelineRepository.findByKeyUserId("follower1")).thenReturn(userTimelines);

        eventHandlingService.handleUserUnfollowed(followEvent);

        verify(followerRepository, times(1)).removeFollower("user123", "follower1");
        verify(userTimelineRepository, times(1)).findByKeyUserId("follower1");
        // Assume delete method is called for each timeline entry
        verify(userTimelineRepository, times(userTimelines.size())).delete(any(UserTimeline.class));
    }

}

