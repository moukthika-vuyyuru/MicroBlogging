package com.microblogging.feed.controller;

import com.microblogging.feed.model.UserTimeline;
import com.microblogging.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user_feed")
@Slf4j
@Tag(name = "User Feed", description = "APIs for managing user feeds")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    // Endpoint to get the user timeline feed
    @GetMapping("/{userId}")
    @Operation(summary = "Get user feed", description = "Retrieve the feed for a specific user")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of user feed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserTimeline.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    @ApiResponse(responseCode = "500", description = "Internal server error")

    public ResponseEntity<?> getFeedForUser(@Parameter(description = "Unique identifier of the user") @PathVariable("userId") String userId,
                                            @Parameter(description = "Maximum number of feed items to return") @RequestParam(value = "limit", required = false, defaultValue = "100") int limit,
                                            @Parameter(description = "Fetch feed items older than this timestamp") @RequestParam(value = "older_than", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime olderThan) {
        try {
            log.info("Helloo! In FeedController.getFeedForUser() ");
            log.info("Retrieving feed for user {}", userId);
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.badRequest().body("UserId cannot be null or empty");
            }
            if (limit <= 0) {
                return ResponseEntity.badRequest().body("Limit must be greater than 0");
            }
            List<UserTimeline> timeline = feedService.getUserTimeline(userId, limit, olderThan);
            return ResponseEntity.ok(timeline);
        } catch (Exception e) {
            log.error("Error retrieving feed for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

