package com.microblogging.post.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class PostToKafkaMessageMapper {

	public String writeValueAsJson(Post post, String eventType) throws JsonProcessingException
	{
		KafkaMessage message = new KafkaMessage();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    
		switch(eventType)
		{
			case "tweet_created":
								 mapPostToKafkaMessage(post,message,eventType);
								 return mapper.writeValueAsString(message);
			case "tweet_updated":
								 mapPostToKafkaMessage(post,message,eventType);
								 return mapper.writeValueAsString(message);
			case "tweet_deleted":
								 mapPostToKafkaMessage(post,message,eventType);
								 message.setTimestamp(post.getLastModifiedDate());
								 return mapper.writeValueAsString(message);
							
		}
		return null;
	}
	private void mapPostToKafkaMessage(Post post, KafkaMessage message, String eventType)
	{
		message.setEventType(eventType);
		message.setContent(post.getContent());
		 message.setTimestamp(post.getCreatedDate());
		 message.setTweetId(post.getPostId());
		 message.setTimestampUpdated(post.getLastModifiedDate());
		 message.setUserId(post.getUserId());
		 message.setAuthorId(post.getAuthorId());
		 message.setLikes(post.getLikes());
	}
}
