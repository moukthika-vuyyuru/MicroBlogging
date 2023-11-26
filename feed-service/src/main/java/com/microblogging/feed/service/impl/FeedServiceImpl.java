package com.microblogging.feed.service.impl;

import com.microblogging.feed.model.UserTimeline;
import com.microblogging.feed.repository.UserTimelineRepository;
import com.microblogging.feed.service.FeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final UserTimelineRepository userTimelineRepository;

    @Autowired
    public FeedServiceImpl(UserTimelineRepository userTimelineRepository) {
        this.userTimelineRepository = userTimelineRepository;
    }

    @Override
    public List<UserTimeline> getUserTimeline(String userId, int limit, LocalDateTime olderThan) {
        log.info("In FeedServiceImpl.getUserTimeline()");
        if (olderThan == null) {
            log.info("Retrieving feed for user {} with limit  {}", userId, limit);
            return userTimelineRepository.findByUserIdWithLimit(userId, limit);
        } else {
            log.info("Retrieving feed for user {} older than {}", userId, olderThan);
            return userTimelineRepository.findByUserIdAndOlderThanWithLimit(userId, olderThan, limit);
        }
    }
}

