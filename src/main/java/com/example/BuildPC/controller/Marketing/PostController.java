package com.example.BuildPC.controller.Marketing;


import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.service.PostService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model)
    {
        List<PostDto> posts = postService.findAllPost();
        model.addAttribute("posts", posts);
        return "marketing/dashboard";
    }

    @GetMapping("/createPost")
    public String newPostForm(Model model){
        PostDto postDto = new PostDto();
        model.addAttribute("post", postDto);
        return "marketing/create";
    }

    @PostMapping("/createPost")
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto, BindingResult result,
                             Model model){
        if(result.hasErrors()){
            model.addAttribute("post", postDto);
            return "marketing/create";
        }
        postDto.setUrl(getUrl(postDto.getTitle()));
        postService.createPost(postDto);
        return "redirect:/posts/dashboard";
    }

    @GetMapping("/{postId}/edit")
    public String editPostForm(@PathVariable Long postId, Model model){
        PostDto postDto = postService.findPostById(postId);
        model.addAttribute("post", postDto);
        return "marketing/edit";
    }

    @PostMapping("/{postId}")
    public String updatePost(@Valid @ModelAttribute("post") PostDto post,
                             @PathVariable Long postId,
                             BindingResult result,
                             Model model){
        if(result.hasErrors()){
            model.addAttribute("post", post);
            return "marketing/edit";
        }

        post.setId(postId);
        postService.updatePost(post);
        return "redirect:/posts/dashboard";
    }

    @GetMapping("/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId){
        postService.deletePost(postId);
        return "redirect:/posts/dashboard";
    }

    @GetMapping("/{postUrl}/view")
    public String viewPost(@PathVariable String postUrl, Model model){
        PostDto postDto = postService.findPostByUrl(postUrl);
        model.addAttribute("post", postDto);
        return "marketing/view";
    }

    private static String getUrl(String postTitle){
        String title = postTitle.trim().toLowerCase();
        String url = title.replaceAll("\\s+", "-");
        url = url.replaceAll("[^A-Za-z0-9]", "-");
        return url;
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam(value = "query") String query, Model model){
        List<PostDto> posts = postService.searchPosts(query);
        model.addAttribute("posts", posts);
        return  "marketing/dashboard";
    }
}
