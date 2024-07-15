package com.example.BuildPC.controller.Marketing;


import com.example.BuildPC.dto.CommentDto;
import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.service.CommentService;
import com.example.BuildPC.service.PostService;
import com.example.BuildPC.utils.FileUploadUtil;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private PostService postService;
    private CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
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
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult result,
                             Model model) throws IOException {
        if(postDto.getThumbnailImage().isEmpty()){
            result.addError(new FieldError("postDto", "thumbnailImage", "Please select a file"));
        }
        if(postService.findPostByUrl(postDto.getUrl()) != null){
            result.addError(new FieldError("postDto", "postTitle", "Post title already exists"));
        }
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
                             @PathVariable("postId") Long postId,
                             BindingResult result,
                             Model model){
        if(post.getThumbnailImage().isEmpty()){
            result.addError(new FieldError("post", "thumbnailImage", "Please select a file"));
        }
        if(postService.findPostByUrl(post.getUrl()) != null){
            result.addError(new FieldError("post", "postTitle", "Post title already exists"));
        }
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
        CommentDto commentDto = new CommentDto();
        model.addAttribute("post", postDto);
        model.addAttribute("comment", commentDto);
        return "marketing/view";
    }

    private static String getUrl(String postTitle){
        String title = postTitle.trim().toLowerCase();
        String url = title.replaceAll("\\s+", "-");
        url = url.replaceAll("[^A-Za-z0-9]", "-");
        return url;
    }

    @GetMapping("/{postId}/comments")
    public String getCommentsByPostId(@PathVariable Long postId, Model model) {
        List<CommentDto> comments = commentService.findCommentsByPostId(postId);
        PostDto post = postService.findPostById(postId);
        model.addAttribute("post", post);
        model.addAttribute("comments",comments);
        return  "marketing/comments";
    }

//    @GetMapping("/search")
//    public String searchPosts(@RequestParam(value = "query") String query, Model model){
//        List<PostDto> posts = postService.searchPosts(query);
//        model.addAttribute("posts", posts);
//        return  "marketing/dashboard";
//    }

    @GetMapping("/comments")
    public String postComments(Model model){
        List<CommentDto> comments = commentService.findAllComments();
        model.addAttribute("comments",comments);
        return  "marketing/comments";
    }

    @GetMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable Long commentId, Model model){
        commentService.deleteComment(commentId);
        return "redirect:/posts/comments";
    }
}
