package com.microblogging.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private String postId;
    private String userId;
    private String authorId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String content;

}
