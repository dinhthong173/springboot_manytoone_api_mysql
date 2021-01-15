package com.example.jpa.controller;



import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.Post;
import com.example.jpa.repository.PostRespository;



@RestController
public class PostController {

	@Autowired
	private PostRespository postRespository;
	
	@GetMapping("/posts")
	public Page<Post> getAllPosts(Pageable pageable){
		return postRespository.findAll(pageable);
	}
	
	@PostMapping("/posts")
	public Post createPost(@Valid @RequestBody Post post) {
		return postRespository.save(post);
	}
	
	@PutMapping("/posts/{postId}")
	public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
		return postRespository.findById(postId).map(post ->{
			post.setTitle(postRequest.getTitle());
			post.setDescription(postRequest.getDescription());
			post.setContent(postRequest.getContent());
			return postRespository.save(post);
		}).orElseThrow(()-> new ResourceNotFoundException("PostId"+ postId+" not found"));
	}
	
//	@DeleteMapping("/posts/{postId}")
//    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
//        return postRespository.findById(postId).map(post -> {
//            postRespository.delete(post);
//            
//            return ResponseEntity.ok().build();
//        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
//    }
	
	@DeleteMapping("/posts/{postId}")
	public Map<String, Boolean> deletePost(@PathVariable Long postId) throws Exception {
		
		Post post= postRespository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found on:"+ postId));	
		postRespository.delete(post);
		Map<String, Boolean> response= new HashMap<>();
		response.put("delete", Boolean.TRUE);
		return response;
	}
}
