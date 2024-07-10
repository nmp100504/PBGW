package com.example.BuildPC.repository;

import com.example.BuildPC.model.Comment;
import com.example.BuildPC.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}