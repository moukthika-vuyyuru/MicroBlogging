package com.microblogging.consumer.repo;


import java.util.Set;

public interface FollowerRepository {
    void addFollower(String userId, String followerId);
    void removeFollower(String userId, String followerId);
    Set<String> getFollowers(String userId);
}

