package com.microblogging.follow.repository;

import com.microblogging.follow.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserId(String userId);

    @Query("{ 'username': { $regex: ?0, $options: 'i' } }")
    List<User> searchByUsername(String username);
}
