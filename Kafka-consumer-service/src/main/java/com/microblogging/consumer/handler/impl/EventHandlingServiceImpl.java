package com.microblogging.consumer.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microblogging.consumer.handler.EventHandlingService;
import com.microblogging.consumer.model.*;
import com.microblogging.consumer.repo.FollowerRepository;
import com.microblogging.consumer.repo.UserTimelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventHandlingServiceImpl implements EventHandlingService {

    private final UserTimelineRepository userTimelineRepository;
    private final ObjectMapper objectMapper;
    private final FollowerRepository followerRepository;

    @Override
    public void handleTweetCreated(TweetMessage tweetMessage) {
        // Handle the "tweet_created" event type here
        log.info("Handling tweet_created event: " + tweetMessage.toString());
        // Fetch the list of followers' IDs for the user who created the tweet
        // Convert TweetMessage to UserTimeline and save it for each follower
        // Fetch the list of followers' IDs for the user who created the tweet
        List<String> followerIds = new ArrayList<>(getFollowerIds(tweetMessage.getUserId()));
        followerIds.forEach(followerId -> {
            try {
                LocalDateTime tweetTimestamp = objectMapper.convertValue(tweetMessage.getTimestamp(), LocalDateTime.class);

                UserTimelineKey key = new UserTimelineKey(
                        followerId,
                        tweetTimestamp,
                        tweetMessage.getTweetId()
                );

                UserTimeline userTimeline = new UserTimeline(
                        key,
                        tweetMessage.getUserId(),
                        tweetMessage.getContent(),
                        new ArrayList<>()
                );

                userTimelineRepository.save(userTimeline);
            } catch (Exception e) {
                log.error("An error occurred while saving the user timeline", e);
            }
        });
    }

    @Override
    public void handleTweetUpdated(TweetMessage tweetMessage) {
        // Fetch the list of followers' IDs for the user who updated the tweet
        List<String> followerIds = new ArrayList<>(getFollowerIds(tweetMessage.getUserId()));
        log.info("Handling tweet_updated event: " + tweetMessage.toString());

        followerIds.forEach(followerId -> {
            try {
                // Delete the old timeline entry from the database
                LocalDateTime tweetTimestamp = objectMapper.convertValue(tweetMessage.getTimestamp(), LocalDateTime.class);
                UserTimelineKey oldKey = new UserTimelineKey(followerId, tweetTimestamp, tweetMessage.getTweetId());
                log.info("Deleting tweet for user to update it {}", followerId);
                userTimelineRepository.deleteById(oldKey);
                log.info("Deleted tweet for user to update it {}", followerId);

                log.info("Updating tweet for user {}", followerId);
                // Insert the new timeline entry with the updated timestamp
                LocalDateTime updatedTimestamp = objectMapper.convertValue(tweetMessage.getTimestampUpdated(), LocalDateTime.class);
                UserTimelineKey newKey = new UserTimelineKey(
                        followerId,
                        updatedTimestamp, // Convert the String updated timestamp to LocalDateTime
                        tweetMessage.getTweetId()
                );
                UserTimeline newTimelineEntry = new UserTimeline(
                        newKey,
                        tweetMessage.getUserId(),
                        tweetMessage.getContent(),
                        tweetMessage.getLikes()
                );
                userTimelineRepository.save(newTimelineEntry);

                log.info("Updated tweet for user {}", followerId);
            } catch (Exception e) {
                log.error("An error occurred while updating the tweet for user {}", followerId, e);
            }
        });
    }


    @Override
    public void handleTweetDeleted(TweetMessage tweetMessage) {
        // Fetch the list of followers' IDs for the user who created the tweet
        log.info("Handling tweet_deleted event: " + tweetMessage.toString());
        List<String> followerIds = new ArrayList<>(getFollowerIds(tweetMessage.getUserId()));

        followerIds.forEach(followerId -> {
            try {
                // Fetch the current timeline entry from the database
                LocalDateTime tweetTimestamp = objectMapper.convertValue(tweetMessage.getTimestamp(), LocalDateTime.class);
                UserTimelineKey key = new UserTimelineKey(followerId, tweetTimestamp, tweetMessage.getTweetId());
                userTimelineRepository.deleteById(key);
                log.info("Deleted tweet for user {}", followerId);
            } catch (Exception e) {
                log.error("An error occurred while deleting the tweet for user {}", followerId, e);
            }
        });
    }

    @Override
    public void handleUserFollowed(FollowEvent followEvent) {
        log.info("Handling user_followed event: " + followEvent.toString());
        try {
            followerRepository.addFollower(followEvent.getUserId(), followEvent.getFollowerId());
            // Fetch past posts of the followed user
            String followedUserId = followEvent.getUserId();
            List<Post> pastPosts = fetchPastPosts(followedUserId);

            // Update the follower's timeline with these posts
            pastPosts.forEach(post -> updateFollowerTimeline(followEvent.getFollowerId(), post));

        } catch (Exception e) {
            log.error("An error occurred while adding a follower", e);
        }
    }

    @Override
    public void handleUserUnfollowed(FollowEvent followEvent) {
        log.info("Handling user_unfollowed event: " + followEvent.toString());
        try {
            followerRepository.removeFollower(followEvent.getUserId(), followEvent.getFollowerId());

            // Remove the unfollowed user's posts from the follower's timeline
            removePostsFromTimeline(followEvent.getUserId(), followEvent.getFollowerId());

        } catch (Exception e) {
            log.error("An error occurred while removing a follower", e);
        }
    }

    private void removePostsFromTimeline(String authorId, String followerId) {
        try {
            List<UserTimeline> userTimelines = userTimelineRepository.findByKeyUserId(followerId);
            if (userTimelines != null) {
                userTimelines.stream()
                        .filter(ut -> ut != null && authorId.equals(ut.getAuthor_id()))
                        .forEach(userTimelineRepository::delete);
            }
        } catch (Exception e) {
            log.error("An error occurred while removing posts from timeline for user {}", followerId, e);
        }
    }


    private Set<String> getFollowerIds(String userId) {
        return followerRepository.getFollowers(userId);
    }

    private List<Post> fetchPastPosts(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://post-service/api/post/getUserPosts/" + userId;

        try {
            ResponseEntity<Post[]> response = restTemplate.getForEntity(url, Post[].class);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            log.error("Error fetching past posts for userId: " + userId, e);
            return Collections.emptyList(); // Or handle the exception as per your requirement
        }
    }

    private void updateFollowerTimeline(String followerId, Post post) {
        try {
            UserTimelineKey key = new UserTimelineKey(
                    followerId,
                    post.getLastModifiedDate(),
                    post.getPostId()
            );

            UserTimeline userTimeline = new UserTimeline(
                    key,
                    post.getAuthorId(),
                    post.getContent(),
                    post.getLikes()
            );

            userTimelineRepository.save(userTimeline);
        } catch (Exception e) {
            log.error("An error occurred while updating the follower timeline", e);
            // Handle the exception as per your requirement
        }
    }
}

