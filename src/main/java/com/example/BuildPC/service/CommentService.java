package com.example.BuildPC.service;

import com.example.BuildPC.dto.CommentDto;

public interface CommentService {
    void createComment(String postUrl, CommentDto commentDto);
}
