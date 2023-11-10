package com.microblogging.consumer.repo.impl;

import com.microblogging.consumer.repo.FollowerRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class FollowerRepositoryImpl implements FollowerRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public FollowerRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addFollower(String userId, String followerId) {
        redisTemplate.opsForSet().add("followers:" + userId, followerId);
    }

    @Override
    public void removeFollower(String userId, String followerId) {
        redisTemplate.opsForSet().remove("followers:" + userId, followerId);
    }

    @Override
    public Set<String> getFollowers(String userId) {
        return redisTemplate.opsForSet().members("followers:" + userId);
    }
}
