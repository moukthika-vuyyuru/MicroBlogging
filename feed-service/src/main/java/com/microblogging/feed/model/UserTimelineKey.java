package com.microblogging.feed.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyClass
public class UserTimelineKey implements Serializable {

    @PrimaryKeyColumn(name = "user_id", type = PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name = "tweet_timestamp", type = CLUSTERED)
    private LocalDateTime tweetTimestamp;

    @PrimaryKeyColumn(name = "tweet_id", type = CLUSTERED)
    private String tweetId;

}
