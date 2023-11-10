package com.microblogging.feed.service;


import com.microblogging.feed.model.UserTimeline;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedService {
        List<UserTimeline> getUserTimeline(String userId, int limit, LocalDateTime olderThan);
}






