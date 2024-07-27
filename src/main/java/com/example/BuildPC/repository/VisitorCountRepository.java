package com.example.BuildPC.repository;


import com.example.BuildPC.model.VisitorCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VisitorCountRepository extends JpaRepository<VisitorCount, Long> {
    VisitorCount findByPostUrl(String postUrl);
    @Query("SELECT SUM(v.count) FROM VisitorCount v")
    Integer getTotalVisitorCount();
}
