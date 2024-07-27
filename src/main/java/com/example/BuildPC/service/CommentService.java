package com.example.BuildPC.service;

import com.example.BuildPC.dto.CommentDto;

import java.util.List;

public interface CommentService {
    void createComment(String postUrl, CommentDto commentDto);
    List<CommentDto> findAllComments();
    void hideComment(Long commentId);
    void displayComment(Long commentId);
    List<CommentDto> findCommentsByPostId(Long postId);
    Long findPostIdByCommentId(Long commentId);
}
