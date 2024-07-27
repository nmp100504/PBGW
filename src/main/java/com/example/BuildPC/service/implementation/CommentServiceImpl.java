package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.CommentDto;
import com.example.BuildPC.mapper.CommentMapper;
import com.example.BuildPC.model.Comment;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.repository.CommentRepository;
import com.example.BuildPC.repository.PostRepository;
import com.example.BuildPC.service.CommentService;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<CommentDto> findAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(CommentMapper::mapToCommentDto).collect(Collectors.toList());
    }

    @Override
    public void hideComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ExpressionException("Comment not found"));
        comment.setHidden(true);
        commentRepository.save(comment);
    }

    @Override
    public void displayComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ExpressionException("Comment not found"));
        comment.setHidden(false);
        commentRepository.save(comment);
    }

    @Override
    public List<CommentDto> findCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(CommentMapper::mapToCommentDto).collect(Collectors.toList());
    }

    @Override
    public Long findPostIdByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ExpressionException("Comment not found"));
        return comment.getPost().getId();
    }
}
