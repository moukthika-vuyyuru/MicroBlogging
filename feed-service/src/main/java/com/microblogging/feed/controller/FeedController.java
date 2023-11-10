package com.microblogging.feed.controller;

import com.microblogging.feed.model.UserTimeline;
import com.microblogging.feed.service.FeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user_feed")
@Slf4j
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    // Endpoint to get the user timeline feed
    @GetMapping("/{userId}")
    public ResponseEntity<?> getFeedForUser(@PathVariable("userId") String userId,
                                                             @RequestParam(value = "limit", required = false, defaultValue = "100") int limit,
                                                             @RequestParam(value = "older_than", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime olderThan) {
        try {
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.badRequest().body("UserId cannot be null or empty");
            }
            if (limit <= 0) {
                return ResponseEntity.badRequest().body("Limit must be greater than 0");
            }
            List<UserTimeline> timeline = feedService.getUserTimeline(userId, limit, olderThan);
            return ResponseEntity.ok(timeline);
        } catch (Exception e) {
            log.error("Error retrieving feed for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

