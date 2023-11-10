package com.microblogging.consumer.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowEvent {
    private String eventType;
    private String userId;
    private String followerId;
    private LocalDateTime timestamp;
}
