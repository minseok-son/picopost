package com.example.picopost.post.repository;

import com.example.picopost.post .model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository interface for Post entities.
 * Extends JpaRepository to inherit standard CRUD and pagination methods.
 * <Post, Long> specifies the Entity type (Post) and the type of its primary key (Long).
 */
@Repository // 1. Marks this interface as a Spring Data repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // ----------------------------------------------------------------------
    // Standard CRUD methods are automatically inherited, including:
    // - save(Post post)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)
    // - findAll(Pageable pageable)
    // ----------------------------------------------------------------------


    /**
     * Custom Query Method: Find all posts created by a specific user.
     * Spring Data JPA automatically generates the necessary SQL query
     * based on the method name format: 'findBy[FieldName]'.
     *
     * @param authorId The ID of the author (from the Auth Service).
     * @return A list of posts belonging to that author.
     */
    List<Post> findByAuthorId(Long authorId);

    void deleteAllByAuthorId(Long authorId);
}
