package com.microblogging.consumer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TweetMessage {
    private String eventType;
    private String timestamp; // Created timestamp
    private String timestampUpdated; // Updated timestamp
    private String userId;
    private String tweetId;
    private String content;

    // No-arg constructor
    public TweetMessage() {
    }

    // All-args constructor
    @JsonCreator
    public TweetMessage(
            @JsonProperty("eventType") String eventType,
            @JsonProperty("timestamp") String timestamp,
            @JsonProperty("userId") String userId,
            @JsonProperty("tweetId") String tweetId,
            @JsonProperty("content") String content,
            @JsonProperty("timestampUpdated") String timestampUpdated) {
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.userId = userId;
        this.tweetId = tweetId;
        this.content = content;
        this.timestampUpdated = timestampUpdated;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestampUpdated() {
        return timestampUpdated;
    }

    public void setTimestampUpdated(String timestampUpdated) {
        this.timestampUpdated = timestampUpdated;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TweetMessage{" +
                "eventType='" + eventType + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", timestampUpdated='" + timestampUpdated + '\'' +
                ", userId='" + userId + '\'' +
                ", tweetId='" + tweetId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}