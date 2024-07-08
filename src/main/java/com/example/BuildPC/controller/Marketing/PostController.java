package com.example.BuildPC.controller.Marketing;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;
import com.example.BuildPC.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
//    @GetMapping("")
//    public String getAllPosts(
//            @RequestParam(defaultValue = "0") Integer pageNo,
//            @RequestParam(defaultValue = "10") Integer pageSize,
//            @RequestParam(defaultValue = "") String search,
//            Model model) {
//        List<Post> listPost = postService.findAllPost(pageNo, pageSize, search);
//        model.addAttribute("posts", listPost);
//        return "marketing/list"; // View for listing posts
//    }
//
//    @GetMapping("/{id}")
//    public String getPostById(@PathVariable int id, Model model) {
//        Post post = postService.findPostById(id);
//        model.addAttribute("post", post);
//        return "marketing/details"; // View for post details
//    }
//
//    @GetMapping("/create")
//    public String viewCreatePostPage(Model model) {
//        model.addAttribute("post", new Post());
//        return "marketing/create"; // View for creating a post
//    }
//
//    @PostMapping("/create")
//    public String createPost(@Valid @ModelAttribute("post") Post post, BindingResult result, RedirectAttributes redirectAttributes) {
//        if (result.hasErrors()) {
//            return "marketing/create"; // Return to the creation form if validation fails
//        }
//
//        Optional<User> optionalUser = postService.getCurrentUser();
//        if (optionalUser.isPresent()) {
//            post.setUser(optionalUser.get());
//            postService.createNewPost(post);
//            redirectAttributes.addFlashAttribute("message", "Post created successfully");
//            return "redirect:/posts";
//        } else {
//            redirectAttributes.addFlashAttribute("error", "User not found. Please log in and try again.");
//            return "redirect:/login";
//        }
//    }
//
//    @GetMapping("/update/{id}")
//    public String viewUpdatePostPage(@PathVariable int id, Model model) {
//        Post post = postService.findPostById(id);
//        model.addAttribute("post", post);
//        return "marketing/update"; // View for updating a post
//    }
//
//    @PostMapping("/update/{id}")
//    public String updatePost(@PathVariable int id, @Valid @ModelAttribute("post") Post post, BindingResult result, RedirectAttributes redirectAttributes) {
//        if (result.hasErrors()) {
//            return "marketing/update"; // Return to the update form if validation fails
//        }
//
//        Optional<User> optionalUser = postService.getCurrentUser();
//        if (optionalUser.isPresent()) {
//            post.setUser(optionalUser.get());
//            postService.updatePost(post);
//            redirectAttributes.addFlashAttribute("message", "Post updated successfully");
//            return "redirect:/posts";
//        } else {
//            redirectAttributes.addFlashAttribute("error", "User not found. Please log in and try again.");
//            return "redirect:/login";
//        }
//    }
//
//    @PostMapping("/delete/{id}")
//    public String deletePost(@PathVariable int id, RedirectAttributes redirectAttributes) {
//        try {
//            postService.deletePostById(id);
//            redirectAttributes.addFlashAttribute("message", "Post deleted successfully");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//        }
//        return "redirect:/posts";
//    }
}
