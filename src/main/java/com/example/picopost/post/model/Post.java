package com.example.picopost.post.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA Entity representing a 'post' in the database.
 */
@Entity // 1. Marks this class as a JPA entity
@Table(name = "posts") // Maps this entity to a table named 'posts'
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: Generates a no-argument constructor (required by JPA)
public class Post {

    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID
    private Long id;

    // Content Fields
    @Column(nullable = false, length = 100) // Ensures the title cannot be null
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT") // Use TEXT for long content
    private String content;

    // Metadata Fields

    // Author ID (Foreign Key reference to the separate Auth/User service)
    @Column(nullable = false)
    private Long authorId; 

    // Auditing Fields (Managed automatically by Hibernate)
    @CreationTimestamp // Automatically sets the time on creation
    @Column(nullable = false, updatable = false) // Ensures it cannot be updated later
    private LocalDateTime createdAt;

    @UpdateTimestamp // Automatically updates the time on modification
    private LocalDateTime updatedAt;
}