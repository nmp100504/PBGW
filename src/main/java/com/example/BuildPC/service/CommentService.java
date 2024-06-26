package com.example.BuildPC.service;

import com.example.BuildPC.model.Comment;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByProduct(Product product) {
        return commentRepository.findByProduct(product);
    }

    public Comment saveComment(Comment comment) {
        comment.setDate(LocalDateTime.now());
        return commentRepository.save(comment);
    }
}
