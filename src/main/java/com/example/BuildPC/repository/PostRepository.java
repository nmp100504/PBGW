package com.example.BuildPC.repository;


import com.example.BuildPC.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUrl(String postUrl);

    @Query("select p from Post p WHERE  " +
    " p.title LIKE CONCAT('%', :query, '%') OR" +
    " p.shortDescription LIKE CONCAT('%', :query, '%')")
    List<Post> searchPosts(String query);

}
