package com.example.BuildPC.service;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    List<PostDto> findSortedPost(String field);

    Page<PostDto> findSortedPaginatedPost(String field, int pageSize);

    Page<PostDto> findSortedPaginatedPostByAuthor(String field, Long authorId, int pageSize);

    Page<PostDto> findThreeMostRecentPostsByAuthor(Long authorId);

    List<PostDto> searchPosts(String query);

    List<PostDto> findPostsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime);

    Map<String, List<PostDto>> findPostsGroupedByMonthInYear();

    Map<Integer, List<PostDto>> findPostsGroupedByWeekInMonth();

    Map<String, List<PostDto>> findPostsGroupedByDayInWeek();
}
