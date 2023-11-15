package com.microblogging.post.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.microblogging.post.model.Post;
import com.microblogging.post.model.PostToKafkaMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    PostToKafkaMessageMapper mapper = new PostToKafkaMessageMapper();
    
    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Post post, String eventType) {
    	
    	String json = null;
		try {
			json = mapper.writeValueAsJson(post, eventType);
            log.info("Sending message to kafka topic: "+json);
		} catch (JsonProcessingException e) {
			log.error("Parsing exception while sending message to kafka topic : {}",e.getMessage());
		}
        kafkaTemplate.send("tweet-updates-topic", json);
        log.info("Message sent to kafka topic");
    }
}