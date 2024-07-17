package com.example.BuildPC.service;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {
    List<PostDto> findAllPost();

    void createPost(PostDto postDto, MultipartFile thumbnail);

    PostDto findPostById(long id);

    void updatePost(PostDto postDto, MultipartFile thumbnail);

    void deletePost(long id);

    PostDto findPostByUrl(String postUrl);

    Page<PostDto> findPaginatedPost(int pageNo, int pageSize);

    List<PostDto> getTop3RecentPosts();
//    List<PostDto> getMostRecentPosts(int limit);
//
//    List<PostDto> getMostRecentPostsByAuthor(Long authorId, int limit);

}
