//package com.example.BuildPC.service;
//
//import com.example.BuildPC.model.Comment;
//import com.example.BuildPC.repository.CommentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    public List<Comment> findByPostId(Integer postId) {
//        return commentRepository.findByPostId(postId);
//    }
//
//    public Comment save(Comment comment) {
//        return commentRepository.save(comment);
//    }
//}
