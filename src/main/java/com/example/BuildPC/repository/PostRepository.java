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
    Optional<Post> findByUrl(String url);
//    @Query(value = "SELECT * FROM post p " +
//            "WHERE (LOWER(p.post_content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(p.post_title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
////            "OR LOWER(p.user_id.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) "
//            , nativeQuery = true)
//    Page<Post> searchPostsAndFilter(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query(value = "SELECT * FROM post p " +
            "WHERE (LOWER(p.post_content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.post_title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY p.post_id LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Post> searchPostsAndFilter(@Param("searchTerm") String searchTerm, @Param("limit") int limit, @Param("offset") int offset);

}
