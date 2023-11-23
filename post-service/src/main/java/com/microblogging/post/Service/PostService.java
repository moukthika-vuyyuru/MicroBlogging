package com.microblogging.post.Service;

import com.microblogging.post.dao.IPostDao;
import com.microblogging.post.model.Post;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PostService {

	@Autowired
    private IPostDao repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public Post addPost(Post post)
	{
		return repository.save(post);
	}
	
	public List<Post> getPosts()
	{
		return repository.findAll();
	}
	
	public Optional<Post> getPostByPostId(String postId)
	{
		return repository.findById(postId);
	}
	
	public List<Post> getPostsByUserId(String userId)
	{
		return repository.findByUserId(userId);
	}
	
	public Optional<Post> updatePostByPostId(String userId, String postId, Post post) {
	    Optional<Post> postToBeUpdated = repository.findById(postId);
	   
	    if (postToBeUpdated.isPresent()) {
	    	if(!userId.equals(postToBeUpdated.get().getAuthorId()))
	    	{
	    		System.out.println("A user cannot update other user's posts");
		        return Optional.empty(); 
	    	}
	        Post updatedPost = postToBeUpdated.map(p -> {
	            p.setContent(post.getContent());
	            return p;
	        }).orElse(null); 

	        repository.save(updatedPost);
	        return Optional.of(updatedPost); 
	    } else {
	        System.out.println("Was not able to find the post to update with ID value " + postId);
	        return Optional.empty(); 
	    }
	}

	
	public boolean deletePostByPostId(String userId, String postId)
	{
		Optional<Post> postToBeDeleted = repository.findById(postId);
		
		if(postToBeDeleted.isPresent())
		{
			if(!userId.equals(postToBeDeleted.map(p -> p.getUserId()).orElse(null)))
				System.out.println("A user cannot delete other user's posts");
			else
			{
				repository.deleteById(postId);
				return true;
			}
		}
		else
			System.out.println("Did not find the post to be deleted");
		return false;
	}
	public List<Post> getAllPosts() {
		List<Post> posts = repository.findAll();
		return posts;
	}
	public List<Post> searchPosts(LocalDateTime fromDate, LocalDateTime toDate, String userId, String content) {
		List<Criteria> criteriaList = new ArrayList<>();

		if (fromDate != null) {
			criteriaList.add(Criteria.where("createdDate").gte(fromDate));
		}
		if (toDate != null) {
			criteriaList.add(Criteria.where("createdDate").lte(toDate));
		}
		if (userId != null && !userId.isEmpty()) {
			criteriaList.add(Criteria.where("userId").is(userId));
		}
		if (content != null && !content.isEmpty()) {
			criteriaList.add(Criteria.where("content").regex("^" + Pattern.quote(content), "i"));
		}

		if (criteriaList.isEmpty()) {
			return mongoTemplate.findAll(Post.class);
		}

		Criteria combinedCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
		Query query = Query.query(combinedCriteria);
		return mongoTemplate.find(query, Post.class);

	}

	public void LikePost(String postId, String userId) {
		Post post = getPostByPostId(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with postId: " + postId));

	    post.getLikes().add(userId);

		repository.save(post);
	}

	public void unLikePost(String postId, String userId) {
		Post post = getPostByPostId(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with postId: " + postId));

		post.getLikes().remove(userId);

		repository.save(post);
	}
}
