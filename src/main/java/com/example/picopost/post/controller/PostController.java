package com.example.picopost.post.controller;

import com.example.picopost.auth.model.User;
import com.example.picopost.post.dto.PostRequest;
import com.example.picopost.post.dto.PostResponse;
import com.example.picopost.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest, @AuthenticationPrincipal User user) {
        Long userId = user.getId();
        PostResponse createdPost = postService.createPost(postRequest, userId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<PostResponse> posts = postService.findAll(page, size);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse post = postService.findById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest, @AuthenticationPrincipal Long userId) {
        PostResponse updatedPost = postService.updatePost(id, postRequest, userId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        postService.deletePost(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
