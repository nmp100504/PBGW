package com.example.BuildPC.repository;


import com.example.BuildPC.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUrl(String Url);
    @Query("SELECT p FROM Post p ORDER BY p.createdOn DESC")
    List<Post> findTop3ByOrderByCreatedOnDesc();

}
