package com.example.BuildPC.controller;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;
import com.example.BuildPC.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("")
    public String getAllPosts(
            @RequestParam(defaultValue = "0") Integer pageNo,
           @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String search,
            Model model) {
        List<Post> listPost = postService.findAllPost(pageNo, pageSize, search);
        model.addAttribute("posts", listPost);
        return "marketing/tables";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable int id, Model model) {
        Post post = postService.findPostById(id);
        model.addAttribute("post", post);
        return "/marketing/postDetail"; // Create a new view for post details
    }

    @GetMapping("/create")
    public String viewCreatePostPage(Model model) {
        model.addAttribute("post", new Post());
        return "marketing/createPost"; // Create a new view for creating a post
    }


    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("post") Post post, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "marketing/createPost"; // Return to the creation form if validation fails
        }

        Optional<User> optionalUser = postService.getCurrentUser();
        if (optionalUser.isPresent()) {
            post.setUser(optionalUser.get());
            postService.createNewPost(post);
            redirectAttributes.addFlashAttribute("message", "Post created successfully");
            return "redirect:/posts";
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found. Please log in and try again.");
            return "redirect:/login";
        }
    }

    @GetMapping("/update/{id}")
    public String viewUpdatePostPage(@PathVariable int id, Model model) {
        Post post = postService.findPostById(id);
        model.addAttribute("post", post);
        return "marketing/updatePost"; // Create a new view for updating a post
    }

    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable int id, @Valid @ModelAttribute("post") Post post, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "marketing/updatePost"; // Return to the update form if validation fails
        }

        Optional<User> optionalUser = postService.getCurrentUser();
        if (optionalUser.isPresent()) {
            post.setUser(optionalUser.get());
            postService.updatePost(post);
            redirectAttributes.addFlashAttribute("message", "Post updated successfully");
            return "redirect:/posts";
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found. Please log in and try again.");
            return "redirect:/login";
        }
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try{
            postService.deletePostById(id);
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/posts";
        }
        redirectAttributes.addFlashAttribute("message", "Post deleted successfully");
        return "redirect:/posts";
    }
}
