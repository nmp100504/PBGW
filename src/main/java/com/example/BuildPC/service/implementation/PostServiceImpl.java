package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.mapper.CommentMapper;
import com.example.BuildPC.mapper.PostMapper;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;
import com.example.BuildPC.model.Vote;
import com.example.BuildPC.repository.PostRepository;
import com.example.BuildPC.repository.UserRepository;
import com.example.BuildPC.repository.VoteRepository;
import com.example.BuildPC.service.PostService;
import com.example.BuildPC.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
//    private final Path storageLocation = Paths.get("public/images/thumbnail/"); // Set your upload directory

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public List<PostDto> findAllPost() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostMapper::mapToPostDTO).toList();
    }

    @Override
    public void createPost(PostDto postDto, MultipartFile thumbnail) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> user = userRepository.findByEmail(email);

        Post post = PostMapper.mapToPost(postDto);
        if (thumbnail != null && !thumbnail.isEmpty()) {
            try {
                post.setThumbnailData(thumbnail.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save thumbnail image", e);
            }
        }

        if (user.isPresent()) {
            post.setCreatedBy(user.get());
            postRepository.save(post);
        }
    }

    @Override
    public PostDto findPostById(long id) {
        Post post = postRepository.findById(id).get();
        return PostMapper.mapToPostDTO(post);
    }

    @Override
    public void updatePost(PostDto postDto, MultipartFile thumbnail) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> user = userRepository.findByEmail(email);

        Post existingPost = postRepository.findById(postDto.getId()).orElseThrow(() -> new RuntimeException("Post not found"));

        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        existingPost.setUrl(postDto.getUrl());
        existingPost.setShortDescription(postDto.getShortDescription());
        existingPost.setCreatedOn(postDto.getCreatedOn());
        existingPost.setUpdatedOn(postDto.getUpdatedOn());
        existingPost.setComments(postDto.getComments().stream()
                .map(CommentMapper::mapToComment)
                .collect(Collectors.toSet()));

        if (thumbnail != null && !thumbnail.isEmpty()) {
            try {
                existingPost.setThumbnailData(thumbnail.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save thumbnail image", e);
            }
        }

        if (user.isPresent()) {
            existingPost.setCreatedBy(user.get());
            postRepository.save(existingPost);
        }
    }

    @Override
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostDto findPostByUrl(String postUrl) {
        Post post = postRepository.findByUrl(postUrl).get();
        return PostMapper.mapToPostDTO(post);
    }

//    @Override
//    public List<PostDto> searchPosts(String query) {
//        List<Post> posts = postRepository.searchPosts(query);
//        return posts.stream().map(PostMapper::mapToPostDTO).collect(Collectors.toList());
//    }

    @Override
    public Page<PostDto> findPaginatedPost(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo -1, pageSize);
        return this.postRepository.findAll(pageable).map(PostMapper::mapToPostDTO);
    }

    @Override
    public List<PostDto> findSortedPost(String field) {
        List<Post> sortedPosts = postRepository.findAll(Sort.by(Sort.Direction.ASC, field));
        return sortedPosts.stream()
                .map(PostMapper::mapToPostDTO)
                .collect(Collectors.toList());
    }
    @Override
    public Page<PostDto> findSortedPaginatedPost(String field, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, field));
        return postRepository.findAll(pageable).map(PostMapper::mapToPostDTO);
    }

    @Override
    public Page<PostDto> findSortedPaginatedPostByAuthor(String field, Long authorId, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, field));
        return postRepository.findAllByCreatedById(authorId, pageable).map(PostMapper::mapToPostDTO);
    }

    @Override
    public Page<PostDto> findThreeMostRecentPostsByAuthor(Long authorId) {
        return findSortedPaginatedPostByAuthor("createdOn", authorId, 3);
    }

    @Override
    public List<PostDto> searchPosts(String query) {
        List<Post> posts = postRepository.searchPosts(query);
        return posts.stream().map(PostMapper::mapToPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<PostDto> findPostsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Post> posts = postRepository.findByCreatedOnBetween(startDateTime, endDateTime);
        return posts.stream().map(PostMapper::mapToPostDTO).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<PostDto>> findPostsGroupedByDayInWeek() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, List<PostDto>> postsByDay = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDateTime startOfDay = now.minusDays(i).toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
            List<PostDto> posts = findPostsByDateRange(startOfDay, endOfDay);
            postsByDay.put(startOfDay.getDayOfWeek().name(), posts);
        }
        return postsByDay;
    }

    @Override
    public Map<Integer, List<PostDto>> findPostsGroupedByWeekInMonth() {
        LocalDateTime now = LocalDateTime.now();
        Map<Integer, List<PostDto>> postsByWeek = new HashMap<>();
        for (int i = 0; i < now.getDayOfMonth() / 7; i++) {
            LocalDateTime startOfWeek = now.minusWeeks(i).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
            LocalDateTime endOfWeek = startOfWeek.plusWeeks(1).minusNanos(1);
            List<PostDto> posts = findPostsByDateRange(startOfWeek, endOfWeek);
            postsByWeek.put(i + 1, posts);
        }
        return postsByWeek;
    }

    @Override
    public Map<String, List<PostDto>> findPostsGroupedByMonthInYear() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, List<PostDto>> postsByMonth = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            LocalDateTime startOfMonth = now.minusMonths(i).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
            List<PostDto> posts = findPostsByDateRange(startOfMonth, endOfMonth);
            postsByMonth.put(startOfMonth.getMonth().name(), posts);
        }
        return postsByMonth;
    }
}
