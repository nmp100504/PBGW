package com.example.BuildPC.service;

import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;
import com.example.BuildPC.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;


    @Override
    public List<Post> findAllPost(Integer pageNo, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();
        return postRepository.searchPostsAndFilter(search, limit, offset);
    }

    @Override
    public Post findPostById(Integer postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public Post createNewPost(Post post) {
       return postRepository.save(post);
    }

    @Override
    public Boolean updatePost(Post post) {
        Optional<Post> existingPost = postRepository.findById(post.getId());
        if (existingPost.isPresent()) {
            postRepository.save(post);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deletePostById(int id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return userService.findByEmail(email);
    }

}