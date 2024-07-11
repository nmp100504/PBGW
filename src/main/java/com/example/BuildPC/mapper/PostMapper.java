package com.example.BuildPC.mapper;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.model.Post;

import java.util.stream.Collectors;

public class PostMapper {
    public static PostDto mapToPostDTO(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .url(post.getUrl())
                .shortDescription(post.getShortDescription())
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .comments(post.getComments().stream()
                        .map((comment) -> CommentMapper.mapToCommentDto(comment))
                                .collect(Collectors.toSet()))
//                .thumbnail(post.getThumbnail())
                .build();
    }

    public static Post mapToPost(PostDto postDto) {
        return Post.builder()
                .id(postDto.getId())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .url(postDto.getUrl())
                .shortDescription(postDto.getShortDescription())
                .createdOn(postDto.getCreatedOn())
                .updatedOn(postDto.getUpdatedOn())
                .comments(postDto.getComments().stream()
                        .map((comment) -> CommentMapper.mapToComment(comment))
                        .collect(Collectors.toSet()))
//                .thumbnail(postDto.getThumbnail())
                .build();
    }
}
