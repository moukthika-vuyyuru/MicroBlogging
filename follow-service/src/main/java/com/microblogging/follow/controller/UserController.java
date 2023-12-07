package com.microblogging.follow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microblogging.follow.model.KafkaFollowMessageDetails;
import com.microblogging.follow.model.User;
import com.microblogging.follow.service.KafkaUserFollowerDetailsProducerService;
import com.microblogging.follow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final KafkaUserFollowerDetailsProducerService kafkaUserFollowerDetailsProducerService;

    @Autowired
    public UserController(UserService userService, KafkaUserFollowerDetailsProducerService kafkaUserFollowerDetailsProducerService) {
        this.userService = userService;
        this.kafkaUserFollowerDetailsProducerService = kafkaUserFollowerDetailsProducerService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            log.info("Received request to create a new user with username: {}", user.getUsername());
            User createdUser = userService.createUser(user);
            log.info("Created new user with id: {}", createdUser.getId());
            return ResponseEntity.ok(createdUser);
        }
        catch (Exception e) {
            log.error("Error creating user: A user with username '{}' already exists.", user.getUsername());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A user with the username '" + user.getUsername() + "' already exists.");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
        try {
            log.info("Received request to update user: {}", userId);
            User user = userService.updateUser(userId, updatedUser);
            return ResponseEntity.ok(user);
        }
        catch (Exception e)
        {
            log.error("Error Updating username: A user with username '{}' already exists.", updatedUser.getUsername());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A user with the username '" + updatedUser.getUsername() + "' already exists.");

        }
    }

    @PostMapping("/{userId}/follow/{userToFollowUserId}")
    public ResponseEntity<Void> followUser(@PathVariable String userId, @PathVariable String userToFollowUserId) throws JsonProcessingException {
        log.info("Received request for user with username: {} to follow user with name: {}", userId, userToFollowUserId);
        userService.followUser(userId, userToFollowUserId);
        log.info("User with username: {} followed user with id: {}", userId, userToFollowUserId);
        KafkaFollowMessageDetails kafkaFollowMessageDetails = KafkaFollowMessageDetails.builder()
                .userId(userToFollowUserId)
                .eventType("user_followed")
                .followerId(userId)
                .timestamp(LocalDateTime.now())
                .build();
        kafkaUserFollowerDetailsProducerService.sendMessage(kafkaFollowMessageDetails);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unfollow/{userToUnfollowUserId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable String userId, @PathVariable String userToUnfollowUserId) throws JsonProcessingException {
        log.info("Received request for user with username: {} to unfollow user with id: {}", userId, userToUnfollowUserId);
        userService.unfollowUser(userId, userToUnfollowUserId);
        log.info("User with username: {} unfollowed user with id: {}", userId, userToUnfollowUserId);
        KafkaFollowMessageDetails kafkaFollowMessageDetails = KafkaFollowMessageDetails.builder()
                .userId(userToUnfollowUserId)
                .eventType("user_unfollowed")
                .followerId(userId)
                .timestamp(LocalDateTime.now())
                .build();
        kafkaUserFollowerDetailsProducerService.sendMessage(kafkaFollowMessageDetails);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable String userId) {
        log.info("Received request to get followers for user with username: {}", userId);
        List<User> followers = userService.getFollowers(userId);
        log.info("Retrieved {} followers for user with username: {}", followers.size(), userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable String userId) {
        log.info("Received request to get following for user with username: {}", userId);
        List<User> following = userService.getFollowing(userId);
        log.info("Retrieved {} following for user with username: {}", following.size(), userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        log.info("Received request to get user: {}", userId);
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false)  String keyword) {
        log.info("Received request to search for users with keyword: {}", keyword);
        List<User> users = userService.searchUsers(keyword);
        if(users.isEmpty()) {
            log.info("No users found for the keyword: {}", keyword);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Received request to get all  users");
        List<User> users = userService.getAllUsers();
        if(users.isEmpty()) {
            log.info("No users found ");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

}
