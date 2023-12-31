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

    @Query("{" +
            "    $or: [" +
            "        { 'username': { $regex: ?0, $options: 'i' } }," +
            "        { 'email': { $regex: ?0, $options: 'i' } }," +
            "        { 'firstName': { $regex: ?0, $options: 'i' } }," +
            "        { 'lastName': { $regex: ?0, $options: 'i' } }," +
            "        { 'location': { $regex: ?0, $options: 'i' } }," +
            "        { 'tagline': { $regex: ?0, $options: 'i' } }" +
            "    ]" +
            "}")
    List<User> searchByUsername(String username);
}
