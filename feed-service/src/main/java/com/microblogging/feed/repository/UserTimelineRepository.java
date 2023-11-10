package com.microblogging.feed.repository;

import com.microblogging.feed.model.UserTimeline;
import com.microblogging.feed.model.UserTimelineKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserTimelineRepository extends CassandraRepository<UserTimeline, UserTimelineKey> {

    @Query("SELECT * FROM user_timeline WHERE user_id = :userId ORDER BY tweet_timestamp DESC LIMIT :limit")
    List<UserTimeline> findByUserIdWithLimit(@Param("userId") String userId, @Param("limit") int limit);

    // If you need to use older_than along with limit
    @Query("SELECT * FROM user_timeline WHERE user_id = :userId AND tweet_timestamp < :olderThan ORDER BY tweet_timestamp DESC LIMIT :limit")
    List<UserTimeline> findByUserIdAndOlderThanWithLimit(@Param("userId") String userId, @Param("olderThan") LocalDateTime olderThan, @Param("limit") int limit);

}

