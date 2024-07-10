package com.example.BuildPC.controller.Marketing;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BlogController {

    private PostService postService;

    public BlogController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/blog")
    public String viewBlogPosts(Model model){
        List<PostDto> postsResponse = postService.findAllPost();
        model.addAttribute("posts", postsResponse);
        return "marketing/blog";
    }


}
