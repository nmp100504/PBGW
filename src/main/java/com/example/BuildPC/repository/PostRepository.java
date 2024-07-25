package com.example.BuildPC.repository;


import com.example.BuildPC.model.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUrl(String Url);
    Page<Post> findAllByCreatedById(Long authorId, Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.upvotes = p.upvotes + 1 WHERE p.id = :postId")
    void incrementUpvotes(Long postId);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.downvotes = p.downvotes + 1 WHERE p.id = :postId")
    void incrementDownvotes(Long postId);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.upvotes = p.upvotes - 1 WHERE p.id = :postId")
    void decrementUpvotes(Long postId);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.downvotes = p.downvotes - 1 WHERE p.id = :postId")
    void decrementDownvotes(Long postId);
}
