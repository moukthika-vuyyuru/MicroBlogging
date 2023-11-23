package com.microblogging.post.Controller;

import com.microblogging.post.Service.KafkaProducerService;
import com.microblogging.post.Service.PostService;
import com.microblogging.post.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
	@GetMapping("/getAllPosts")
	public ResponseEntity<List<Post>> getAllPostsByUserId()
	{

		log.info("Received request to get all posts");
		List<Post> posts = postService.getAllPosts();
		if(posts.isEmpty()) {
			log.info("No Posts found ");
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(posts);
	}
	@GetMapping("/searchPosts")
	public List<Post> searchPosts(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
			@RequestParam(required = false) String userId,
			@RequestParam(required = false) String content) {
		return postService.searchPosts(fromDate, toDate, userId, content);
	}

	@PostMapping("/posts/{postId}/like")
	public ResponseEntity<Void> likePost(@PathVariable String postId, @RequestParam String userId) {
		postService.LikePost(postId, userId);
        return ResponseEntity.ok().build();
    }
	@PostMapping("/posts/{postId}/unlike")
	public ResponseEntity<Void> unLikePost(@PathVariable String postId, @RequestParam String userId) {
		postService.unLikePost(postId, userId);
     return ResponseEntity.ok().build();
	}
}
