package com.microblogging.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Table("user_timeline")
@AllArgsConstructor
@NoArgsConstructor
public class UserTimeline {

    @PrimaryKey
    @JsonIgnore
    private UserTimelineKey key;

    private String author_id;
    private String content;
    private List<String> likes;

    @JsonProperty("userId")
    public String getUserId() {
        return key.getUserId();
    }

    @JsonProperty("tweetTimestamp")
    public LocalDateTime getTweetTimestamp() {
        return key.getTweetTimestamp();
    }

    @JsonProperty("tweetId")
    public String getTweetId() {
        return key.getTweetId();
    }

    @JsonProperty("likes")
    public List<String> getLikes() {return likes;}

    public void setLikes(List<String> likes) {this.likes = likes;}

}
