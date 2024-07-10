package com.example.BuildPC.controller.Marketing;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/blog")
@Controller
public class BlogController {

    private PostService postService;

    public BlogController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public String viewBlogPosts(Model model){
        List<PostDto> postsResponse = postService.findAllPost();
        model.addAttribute("posts", postsResponse);
        return "marketing/blog";
    }

    @GetMapping("/{postUrl}")
    private String showBlogPost(@PathVariable("postUrl") String postUrl, Model model){
        PostDto postDto = postService.findPostByUrl(postUrl);
        model.addAttribute("post", postDto);
        return "marketing/view";
    }

    @GetMapping("/search")
    public String searchPost(@RequestParam(value = "query") String query, Model model){
        List<PostDto> postsResponse = postService.searchPosts(query);
        model.addAttribute("posts", postsResponse);
        return "marketing/blog";
    }
}
