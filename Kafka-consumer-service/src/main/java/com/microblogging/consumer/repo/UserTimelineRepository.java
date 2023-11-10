package com.microblogging.consumer.repo;

import com.microblogging.consumer.model.UserTimeline;
import com.microblogging.consumer.model.UserTimelineKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTimelineRepository extends CassandraRepository<UserTimeline, UserTimelineKey> {
    List<UserTimeline> findByKeyUserId(String userId);
}
