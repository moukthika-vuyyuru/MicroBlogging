package com.microblogging.post.Controller;

import com.microblogging.post.Service.KafkaProducerService;
import com.microblogging.post.Service.PostService;
import com.microblogging.post.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/post")
@Slf4j
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private KafkaProducerService kafkaProducerService;
	
	@PostMapping("/createPost")
	public Post addPost(@RequestBody Post post)
	{
		var createdPost = postService.addPost(post);
		kafkaProducerService.sendMessage(post,"tweet_created");
		log.info("Post created with id: " + createdPost.getPostId());
		return createdPost;
	}
	
	@GetMapping("/getUserPosts/{userId}")
	public List<Post> getAllPostsByUserId(@PathVariable String userId)
	{
		return postService.getPostsByUserId(userId);
	}
	
	@PutMapping("/updatePost/{userId}/{postId}")
	public Optional<Post> updatePostById(@PathVariable String userId, @PathVariable String postId, @RequestBody Post post)
	{
		var updatedPost = postService.updatePostByPostId(userId, postId, post);
		if(updatedPost.isPresent())
		{
			var postUpdated = updatedPost.get();
			kafkaProducerService.sendMessage(postUpdated,"tweet_updated");
		}
		log.info("Post updated with id: " + postId);
		return updatedPost;
	}
	
	@DeleteMapping("/deletePost/{userId}/{postId}")
	public void deletePostByPostId(@PathVariable String userId, @PathVariable String postId)
	{
		var post = postService.getPostByPostId(postId);
		var isPostDeleted = postService.deletePostByPostId(userId, postId);
		if(post.isPresent() && isPostDeleted)
		{
			var postDeleted = post.get();
			kafkaProducerService.sendMessage(postDeleted,"tweet_deleted");
		}
		log.info("Post deleted with id: " + postId);
	}
}
