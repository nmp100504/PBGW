package com.example.BuildPC.controller.Marketing;


import com.example.BuildPC.dto.CommentDto;
import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.service.CommentService;
import com.example.BuildPC.service.PostService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
                             @RequestParam("thumbnail") MultipartFile thumbnail,
                             BindingResult result,
                             Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("post", postDto);
            return "marketing/create";
        }
        postDto.setUrl(getUrl(postDto.getTitle()));
        postService.createPost(postDto, thumbnail);
        return "redirect:/posts/dashboard";
    }


    @GetMapping("/{postId}/edit")
    public String editPostForm(@PathVariable Long postId, Model model) {
        PostDto postDto = postService.findPostById(postId);
        model.addAttribute("post", postDto);
        return "marketing/edit";
    }

    @PostMapping("/{postId}")
    public String updatePost(@Valid @ModelAttribute("post") PostDto postDTO,
                             @RequestParam("thumbnail") MultipartFile thumbnail,
                             @PathVariable("postId") Long postId,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("post", postDTO);
            return "marketing/edit";
        }
        postDTO.setId(postId);
        postService.updatePost(postDTO, thumbnail);
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

        List<PostDto> recentPosts = postService.findSortedPaginatedPost("createdOn", 3).getContent();
        model.addAttribute("recentPosts", recentPosts);

        List<PostDto> relatedPosts = postService.findThreeMostRecentPostsByAuthor(postDto.getCreatedBy().getId()).getContent();
        model.addAttribute("relatedPosts", relatedPosts);

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
