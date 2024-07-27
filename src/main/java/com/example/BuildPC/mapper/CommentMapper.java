package com.example.BuildPC.mapper;

import com.example.BuildPC.dto.CommentDto;
import com.example.BuildPC.model.Comment;

import static com.example.BuildPC.mapper.PostMapper.mapToPost;
import static com.example.BuildPC.mapper.PostMapper.mapToPostDTO;

public class CommentMapper {
    public static CommentDto mapToCommentDto(Comment comment){
        return  CommentDto.builder()
                .id(comment.getId())
                .name(comment.getName())
                .email(comment.getEmail())
                .content(comment.getContent())
                .createdOn(comment.getCreatedOn())
                .updatedOn(comment.getUpdatedOn())
                .hidden(comment.isHidden())
//                .post(mapToPostDTO(comment.getPost()))
                .build();
    }

    public static Comment mapToComment(CommentDto commentDto){
        return  Comment.builder()
                .id(commentDto.getId())
                .name(commentDto.getName())
                .email(commentDto.getEmail())
                .content(commentDto.getContent())
                .createdOn(commentDto.getCreatedOn())
                .updatedOn(commentDto.getUpdatedOn())
                .hidden(commentDto.isHidden())
//                .post(mapToPost(commentDto.getPost()))
                .build();
    }
}
