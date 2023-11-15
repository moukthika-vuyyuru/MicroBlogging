package com.microblogging.follow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaFollowMessageDetails {
    private String eventType;
    private String userId;
    private String followerId;
    private LocalDateTime timestamp;
}
