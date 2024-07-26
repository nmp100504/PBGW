package com.example.BuildPC.repository;

import com.example.BuildPC.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByUserIdAndPostId(Long userId, Long postId);
}