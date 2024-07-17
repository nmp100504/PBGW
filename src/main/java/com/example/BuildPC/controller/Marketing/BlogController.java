package com.example.BuildPC.controller.Marketing;

import com.example.BuildPC.dto.CommentDto;
import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.service.PostService;
import com.example.BuildPC.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private PostService postService;
    private UserService userService;

    public BlogController(PostService postService, UserService userService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("")
    public String viewBlogPosts(Model model){
        return findPaginated(1, model);
//        List<PostDto> postsResponse = postService.findAllPost();
//        model.addAttribute("posts", postsResponse);
//        return "marketing/blog";
    }

    @GetMapping("/{postUrl}")
    private String showBlogPost(@PathVariable("postUrl") String postUrl, Model model){
        PostDto postDto = postService.findPostByUrl(postUrl);
        UserDto userDto = postDto.getCreatedBy();
        CommentDto commentDto = new CommentDto();
        // Fetch most recent posts
        List<PostDto> recentPosts = postService.getTop3RecentPosts();
        logger.debug("Recent posts passed to model: {}", recentPosts);

        model.addAttribute("user", userDto);
        model.addAttribute("post", postDto);
        model.addAttribute("comment", commentDto);
        model.addAttribute("recentPosts", recentPosts);

        return "marketing/view";
    }

//    @GetMapping("/search")
//    public String searchPost(@RequestParam(value = "query") String query, Model model){
//        List<PostDto> postsResponse = postService.searchPosts(query);
//        model.addAttribute("posts", postsResponse);
//        return "marketing/blog";
//    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo, Model model){
        int pageSize = 5;

        Page<PostDto> page = postService.findPaginatedPost(pageNo, pageSize);
        List<PostDto> postsResponse = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("posts", postsResponse);
        return "marketing/blog";
    }

    @GetMapping("/display")
    public String displayImage(){
        return "marketing/imageDisplay";
    }
}
