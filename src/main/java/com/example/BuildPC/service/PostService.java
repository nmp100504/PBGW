package com.example.BuildPC.service;

import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {
    List<Post> findAllPost(Integer pageNo, Integer pageSize, String search);

    Post findPostById(Integer postId);

    Post createNewPost(Post post);

    Boolean updatePost(Post post);

    Boolean deletePostById(int id);

    Optional<User> getCurrentUser();
}
