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
    @Transactional
    public void upvotePost(Long userId, Long postId) {
        Vote existingVote = voteRepository.findByUserIdAndPostId(userId, postId);
        if (existingVote == null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
            Vote vote = new Vote(user, post, true);
            voteRepository.save(vote);
            postRepository.incrementUpvotes(postId);
        } else if (!existingVote.isUpvote()) {
            existingVote.setUpvote(true);
            voteRepository.save(existingVote);
            postRepository.incrementUpvotes(postId);
            postRepository.decrementDownvotes(postId);
        }
    }

    @Override
    @Transactional
    public void downvotePost(Long userId, Long postId) {
        Vote existingVote = voteRepository.findByUserIdAndPostId(userId, postId);
        if (existingVote == null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
            Vote vote = new Vote(user, post, false);
            voteRepository.save(vote);
            postRepository.incrementDownvotes(postId);
        } else if (existingVote.isUpvote()) {
            existingVote.setUpvote(false);
            voteRepository.save(existingVote);
            postRepository.incrementDownvotes(postId);
            postRepository.decrementUpvotes(postId);
        }
    }
//    @Override
//    public List<PostDto> getTop3RecentPosts() {
//        List<Post> recentPosts = postRepository.findTop3ByOrderByCreatedOnDesc();
//        List<PostDto> postDtos = recentPosts.stream()
//                .map(PostMapper::mapToPostDTO)
//                .collect(Collectors.toList());
//        logger.debug("Top 3 recent posts: {}", postDtos);
//        return postDtos;
//    }

//    @Override
//    public List<PostDto> getMostRecentPosts(int limit) {
//        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdOn"));
//        Page<Post> posts = postRepository.findAll(pageable);
//        if (posts.isEmpty()) {
//            return null;
//        }
//        return posts.stream().map(PostMapper::mapToPostDTO).collect(Collectors.toList());
//    }
//
//    @Override
//    public List<PostDto> getMostRecentPostsByAuthor(Long authorId, int limit) {
//        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdOn"));
//        Page<Post> posts = postRepository.findByCreatedById(authorId, pageable);
//        if (posts.isEmpty()) {
//            return null;
//        }
//        return posts.stream().map(PostMapper::mapToPostDTO).collect(Collectors.toList());
//    }
}
