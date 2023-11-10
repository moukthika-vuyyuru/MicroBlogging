package com.microblogging.feed.service.impl;

import com.microblogging.feed.model.UserTimeline;
import com.microblogging.feed.repository.UserTimelineRepository;
import com.microblogging.feed.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    private final UserTimelineRepository userTimelineRepository;

    @Autowired
    public FeedServiceImpl(UserTimelineRepository userTimelineRepository) {
        this.userTimelineRepository = userTimelineRepository;
    }

    @Override
    public List<UserTimeline> getUserTimeline(String userId, int limit, LocalDateTime olderThan) {
        if (olderThan == null) {
            return userTimelineRepository.findByUserIdWithLimit(userId, limit);
        } else {
            return userTimelineRepository.findByUserIdAndOlderThanWithLimit(userId, olderThan, limit);
        }
    }
}

