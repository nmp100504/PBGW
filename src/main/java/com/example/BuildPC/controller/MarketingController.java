package com.example.BuildPC.controller;


import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MarketingController {
    @Autowired
    private PostRepository postRepository;
 @RequestMapping("/marketing")
    public String marketing() {
    return "marketing/index";
 }
    @GetMapping("/create")
    public String showCreatePostForm(Model model) {
        PostDto postDto=new PostDto();
        model.addAttribute("postDto", postDto);
        return "marketing/CreatePost";
    }
    @GetMapping("/marketing")
    public String showIndex1(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "marketing/index";
    }

}
