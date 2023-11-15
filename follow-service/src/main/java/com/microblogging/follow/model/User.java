package com.microblogging.follow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    private String userId;

    @TextIndexed
    @Indexed(unique = true)
    private String username;

    private String tagline;
    private String location;
    private String email;
    private String profileImageUrl;
    private String coverImageUrl;
    private String firstName;
    private String lastName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<String> followers = new HashSet<>();
    private Set<String> following = new HashSet<>();
}
