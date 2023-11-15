package com.microblogging.post.Service;

import com.microblogging.post.dao.IPostDao;
import com.microblogging.post.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

	@Autowired
    private IPostDao repository;
	
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
}
