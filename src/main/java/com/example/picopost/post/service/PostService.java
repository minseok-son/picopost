package com.example.picopost.post.service;

import com.example.picopost.post.dto.PostRequest;
import com.example.picopost.post.dto.PostResponse;
import com.example.picopost.post.model.Post;
import com.example.picopost.post.repository.PostRepository;
import com.example.picopost.post.exception.PostNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service // Marks this class as a Spring business service component
@Transactional(readOnly = true) // Default transaction setting for read operations
public class PostService {

    private final PostRepository postRepository;
    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    // Constructor injection for the PostRepository dependency
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // --- C R E A T E ---
    @Transactional // Override for write operations
    public PostResponse createPost(PostRequest requestDto, Long authorId) {
        // 1. Convert DTO to Entity (Model)
        Post newPost = new Post();
        newPost.setTitle(requestDto.getTitle());
        newPost.setContent(requestDto.getContent());
        newPost.setAuthorId(authorId); // Set from the authenticated user ID
        newPost.setCreatedAt(LocalDateTime.now());

        // 2. Save the Entity to the database
        Post savedPost = postRepository.save(newPost);

        // 3. Convert the saved Entity back to a Response DTO
        return toResponseDto(savedPost);
    }

    // --- R E A D (Single) ---
    public PostResponse findById(Long id) {
        // Find the entity, or throw a custom exception if not found
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + id));

        // Convert the entity to a Response DTO
        return toResponseDto(post);
    }

    // --- R E A D (All with Pagination) ---
    public List<PostResponse> findAll(int page, int size) {
        // Create a page request for the repository
        PageRequest pageRequest = PageRequest.of(page, size);

        // Fetch entities and map them to DTOs
        return postRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // --- U P D A T E ---
    @Transactional
    public PostResponse updatePost(Long id, PostRequest requestDto, Long userId) {
        // 1. Retrieve existing post (will throw exception if not found)
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + id));

        // 2. Authorization Check (Essential Business Logic)
        if (!post.getAuthorId().equals(userId)) {
            // In a real app, this would be a specific AuthorizationException (403 Forbidden)
            throw new RuntimeException("User is not authorized to update this post.");
        }

        // 3. Apply updates from DTO to Entity
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        // 4. Save the updated Entity
        Post updatedPost = postRepository.save(post);

        // 5. Convert and return
        return toResponseDto(updatedPost);
    }

    // --- D E L E T E ---
    @Transactional
    public void deletePost(Long id, Long userId) {
        // Retrieve and authorize (same check as update)

        log.info("Request received to delete post ID: {} by user ID: {}", id, userId); // MOVE LOG HERE)
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + id));

        if (!post.getAuthorId().equals(userId)) {
            log.error("AUTHORIZATION DENIED: User ID {} attempted to delete post ID {} (Author ID: {})", 
              userId, id, post.getAuthorId());
            throw new RuntimeException("User is not authorized to delete this post.");
        }
        
        postRepository.delete(post);
    }


    // --- M A P P I N G ---
    /**
     * Converts a Post Entity/Model into a PostResponse DTO.
     * This is the bridge between the internal database structure and the public API contract.
     */
    private PostResponse toResponseDto(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorId(post.getAuthorId());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());

        return dto;
    }
}