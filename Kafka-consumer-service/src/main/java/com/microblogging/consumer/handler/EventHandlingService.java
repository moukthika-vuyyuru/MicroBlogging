package com.microblogging.consumer.handler;

import com.microblogging.consumer.model.FollowEvent;
import com.microblogging.consumer.model.TweetMessage;

public interface EventHandlingService {
    void handleTweetCreated(TweetMessage tweetMessage);

    void handleTweetUpdated(TweetMessage tweetMessage);

    void handleTweetDeleted(TweetMessage tweetMessage);

    void handleUserFollowed(FollowEvent followEvent);

    void handleUserUnfollowed(FollowEvent followEvent);
}