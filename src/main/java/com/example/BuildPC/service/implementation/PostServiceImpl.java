package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.mapper.PostMapper;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;
import com.example.BuildPC.repository.PostRepository;
import com.example.BuildPC.service.PostService;
import com.example.BuildPC.service.UserService;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<PostDto> findAllPost() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostMapper::mapToPostDTO)
                .toList();
    }
}
