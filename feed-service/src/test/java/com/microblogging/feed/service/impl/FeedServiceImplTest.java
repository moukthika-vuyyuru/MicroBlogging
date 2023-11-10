package com.microblogging.feed.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.microblogging.feed.model.UserTimeline;
import com.microblogging.feed.model.UserTimelineKey;
import com.microblogging.feed.repository.UserTimelineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FeedServiceImplTest {

    @Mock
    private UserTimelineRepository userTimelineRepository;

    @InjectMocks
    private FeedServiceImpl feedService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserTimelineWithoutOlderThan() {
        String userId = "testUser";
        int limit = 1;
        UserTimeline userTimeline = new UserTimeline();
        UserTimelineKey userTimelineKey = new UserTimelineKey();
        userTimelineKey.setUserId(userId);
        userTimelineKey.setTweetTimestamp(LocalDateTime.now());
        userTimeline.setKey(userTimelineKey);
        userTimeline.setAuthor_id("user2");

        // Mocking the response from the repository
        when(userTimelineRepository.findByUserIdWithLimit(userId, limit)).thenReturn(Arrays.asList(userTimeline));

        // Calling the method under test
        List<UserTimeline> result = feedService.getUserTimeline(userId, limit, null);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetUserTimelineWithOlderThan() {
        String userId = "testUser";
        int limit = 5;
        LocalDateTime olderThan = LocalDateTime.now().minusDays(1);

        // Mocking the response from the repository
        when(userTimelineRepository.findByUserIdAndOlderThanWithLimit(userId, olderThan, limit)).thenReturn(Arrays.asList(/* mocked UserTimeline objects */));

        // Calling the method under test
        List<UserTimeline> result = feedService.getUserTimeline(userId, limit, olderThan);

        // Assertions
        assertNotNull(result);
    }

}
