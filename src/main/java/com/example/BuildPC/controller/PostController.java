package com.example.BuildPC.controller;


import com.example.BuildPC.dtos.PostDto;
import com.example.BuildPC.models.Post;
import com.example.BuildPC.models.User;
import com.example.BuildPC.repository.PostRepository;
import com.example.BuildPC.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostRepository repo;
    @Autowired
    private UserRepository repo1;
    @GetMapping({"","/"})
    public String showPostList(Model model){
        List<Post> posts = repo.findAll();
        model.addAttribute("posts", posts);
        return "posts/index";
    }
    @GetMapping("/index1")
    public String showIndex1(Model model) {
        List<Post> posts = repo.findAll();
        model.addAttribute("posts", posts);
        return "posts/index1";
    }
    //    @GetMapping("/details")
//    public String showPostDetails(Model model, @RequestParam int id) {
//        try {
//            // Retrieve the post from repository by id
//            Post post = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
//
//            // Add the post directly to the model
//            model.addAttribute("post", post);
//        } catch (Exception ex) {
//            System.out.println("Exception: " + ex.getMessage());
//            return "redirect:/posts/index"; // Redirect to post listing page on error
//        }
//        return "/posts/post_details"; // Return the name of the Thymeleaf template
//    }
    @GetMapping("/details")
    public String showPostDetails(Model model, @RequestParam int id) {
        try {
            // Retrieve the post from repository by id
            Post post = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

            // Format the content to replace newline characters with <br /> tags
            String formattedContent = post.getPostConent().replace("\n", "<br />");
            post.setPostConent(formattedContent);

            // Add the post directly to the model
            model.addAttribute("post", post);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/posts"; // Redirect to post listing page on error
        }
        return "posts/post_details"; // Return the name of the Thymeleaf template
    }
//    @GetMapping("/create")
//    public String showCreatePostForm(Model model) {
//        List<User> users = repo1.findAll();
//        model.addAttribute("users", users);
//        model.addAttribute("postDto", new PostDto());
//        return "posts/CreatePost";
//    }
//    @PostMapping("/create")
//    public String createPost(
//            @Valid @ModelAttribute PostDto postDto,
//            BindingResult result,
//            RedirectAttributes redirectAttributes) {
//
//        if (result.hasErrors()) {
//            return "posts/CreatePost"; // Return to the create post form with validation errors
//        }
//
//        MultipartFile image = postDto.getImageFile();
//        Date createdAt = new Date();
//        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
//
//        try {
//            String uploadDir = "public/images";
//            Path uploadPath = Paths.get(uploadDir);
//
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            try (InputStream inputStream = image.getInputStream()) {
//                Files.copy(inputStream, Paths.get(uploadDir + "/" + storageFileName),
//                        StandardCopyOption.REPLACE_EXISTING);
//            }
//
//        } catch (IOException ex) {
//            System.out.println("Exception while uploading file: " + ex.getMessage());
//            return "redirect:/posts/create"; // Redirect to the create post form with error message
//        }
//
//        Post post = new Post();
//        post.setPostTitle(postDto.getTitle());
//        post.setPostConent(postDto.getContent());
//        post.setImageFileName(storageFileName);
//        post.setPostCreatedAt(createdAt);
//
//        User user = repo1.findById(postDto.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        post.setUser(user);
//
//        repo.save(post);
//
//        return "redirect:/posts/index1";
//    }
@GetMapping("/create")
public String showCreatePostForm(Model model) {
    List<User> users = repo1.findAll();
    model.addAttribute("users", users);
    model.addAttribute("postDto", new PostDto());
    return "posts/CreatePost";
}

    @PostMapping("/create")
    public String createPost(
            @Valid @ModelAttribute PostDto postDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "posts/CreatePost"; // Return to the create post form with validation errors
        }

        MultipartFile image = postDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + "/" + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException ex) {
            System.out.println("Exception while uploading file: " + ex.getMessage());
            return "redirect:/posts/create"; // Redirect to the create post form with error message
        }

        Post post = new Post();
        post.setPostTitle(postDto.getTitle());
        post.setPostConent(postDto.getContent());
        post.setImageFileName(storageFileName);
        post.setPostCreatedAt(createdAt);

        User user = repo1.findById(postDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        post.setUser(user);

        repo.save(post);

        return "redirect:/marketing";
    }

    @GetMapping("/edit")
    public String showEditPostForm(Model model, @RequestParam int id) {
        try {
            Post post = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
            model.addAttribute("post", post);

            PostDto postDto = new PostDto();
            postDto.setTitle(post.getPostTitle());
            postDto.setContent(post.getPostConent());
            postDto.setUserId(post.getUser().getId());

            model.addAttribute("postDto", postDto);
            List<User> users = repo1.findAll();
            model.addAttribute("users", users);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/posts";
        }
        return "posts/EditPost";
    }

    @PostMapping("/edit")
    public String updatePost(Model model,
                             @RequestParam int id,
                             @Valid @ModelAttribute PostDto postDto, BindingResult result) {
        try {
            Post post = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
            model.addAttribute("post", post);

            if (result.hasErrors()) {
                List<User> users = repo1.findAll();
                model.addAttribute("users", users);
                return "posts/EditPost";
            }

            post.setPostTitle(postDto.getTitle());
            post.setPostConent(postDto.getContent());

            MultipartFile image = postDto.getImageFile();
            if (image != null && !image.isEmpty()) {
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try {
                    String uploadDir = "public/images";
                    Path uploadPath = Paths.get(uploadDir);

                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    try (InputStream inputStream = image.getInputStream()) {
                        Files.copy(inputStream, Paths.get(uploadDir + "/" + storageFileName),
                                StandardCopyOption.REPLACE_EXISTING);
                    }

                } catch (IOException ex) {
                    System.out.println("Exception while uploading file: " + ex.getMessage());
                    return "redirect:/posts/edit?id=" + id; // Redirect to the edit post form with error message
                }

                post.setImageFileName(storageFileName);
            }

            User user = repo1.findById(postDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

            post.setUser(user);

            repo.save(post);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/marketing";
    }
    @GetMapping("/delete")
    public String deletePost(@RequestParam int id, RedirectAttributes redirectAttributes) {
        try {
            repo.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Post deleted successfully");
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting post");
        }
        return "redirect:/marketing";
    }
}




