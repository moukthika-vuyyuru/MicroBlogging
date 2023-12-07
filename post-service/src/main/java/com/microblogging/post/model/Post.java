package com.microblogging.post.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Posts")
public class Post {

	@Id
	private String postId;

	private String userId;

	private String authorId;

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	private LocalDateTime createdDate;

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	private LocalDateTime lastModifiedDate;

	private String content;

	public String getPostId()
	{
		return postId;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public Set<String> getLikes() {
		return Likes;
	}

	public void setLikes(Set<String> likes) {
		Likes = likes;
	}

	private Set<String> Likes = new HashSet<>();
}