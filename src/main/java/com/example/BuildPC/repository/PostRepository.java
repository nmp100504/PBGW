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
    @Query("select p from Post p WHERE  " +
            " p.title LIKE CONCAT('%', :query, '%') OR" +
            " p.shortDescription LIKE CONCAT('%', :query, '%')")
    List<Post> searchPosts(String query);
    @Query("SELECT DATE(p.createdOn), COUNT(p) FROM Post p WHERE p.createdOn >= :startOfWeek AND p.createdOn < :endOfWeek GROUP BY DATE(p.createdOn)")
    List<Object[]> countPostsByDayInCurrentWeek(LocalDateTime startOfWeek, LocalDateTime endOfWeek);
    @Query("SELECT YEAR(p.createdOn), MONTH(p.createdOn), WEEK(p.createdOn), COUNT(p) FROM Post p WHERE p.createdOn >= :startOfMonth AND p.createdOn < :endOfMonth GROUP BY YEAR(p.createdOn), MONTH(p.createdOn), WEEK(p.createdOn)")
    List<Object[]> countPostsByWeekInCurrentMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth);
    @Query("SELECT YEAR(p.createdOn), MONTH(p.createdOn), COUNT(p) " +
            "FROM Post p WHERE p.createdOn >= :startOfYear AND p.createdOn < :endOfYear " +
            "GROUP BY YEAR(p.createdOn), MONTH(p.createdOn)")
    List<Object[]> countPostsByMonthInCurrentYear(LocalDateTime startOfYear, LocalDateTime endOfYear);
    @Query("SELECT p.createdBy, COUNT(p) as postCount " +
            "FROM Post p " +
            "GROUP BY p.createdBy " +
            "ORDER BY postCount DESC")
    List<Object[]> findTopUsersByPostCount();
    @Query("SELECT p.createdBy, COUNT(p) FROM Post p GROUP BY p.createdBy")
    List<Object[]> countPostsByUser();
}
