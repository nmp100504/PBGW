package com.example.BuildPC.repository;


import com.example.BuildPC.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
