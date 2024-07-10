package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.CommentDto;
import com.example.BuildPC.mapper.CommentMapper;
import com.example.BuildPC.model.Comment;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.repository.CommentRepository;
import com.example.BuildPC.repository.PostRepository;
import com.example.BuildPC.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;


    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void createComment(String postUrl, CommentDto commentDto) {
        Post post = postRepository.findByUrl(postUrl).get();
        Comment comment = CommentMapper.mapToComment(commentDto);
        comment.setPost(post);
        commentRepository.save(comment);
    }
}
