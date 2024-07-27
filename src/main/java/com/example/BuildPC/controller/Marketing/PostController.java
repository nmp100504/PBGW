package com.example.BuildPC.controller.Marketing;


import com.example.BuildPC.dto.CommentDto;
import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.service.CommentService;
import com.example.BuildPC.service.PostService;
import com.example.BuildPC.service.VisitorCountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts")
public class PostController {

    private PostService postService;
    private CommentService commentService;
    private VisitorCountService visitorCountService;

    public PostController(PostService postService, CommentService commentService, VisitorCountService visitorCountService) {
        this.postService = postService;
        this.commentService = commentService;
        this.visitorCountService = visitorCountService;
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<PostDto> posts = postService.findAllPost();
        List<CommentDto> comments = commentService.findAllComments();
        int totalVisitor = visitorCountService.getTotalVisitorCount();
        Map<String, Long> postsCountByDay = postService.getPostsCountByDayInCurrentWeek();
        model.addAttribute("postsCountByDay", postsCountByDay);
        Map<String, Long> postsCountByWeek = postService.getPostsCountByWeekInCurrentMonth();
        model.addAttribute("postsCountByWeek", postsCountByWeek);
        Map<String, Long> postsCountByMonth = postService.getPostsCountByMonthInCurrentYear();
        model.addAttribute("postsCountByMonth", postsCountByMonth);
        Map<String, Long> topUsersByPostCount = postService.getTopUsersByPostCount();
        Map<String, Integer> topPostUrlsByViewCount = visitorCountService.getTopPostUrlsByViewCount();
        Map<String, Long> postsCountByUser = postService.getPostsCountByUser();
        model.addAttribute("postsCountByUser", postsCountByUser);
        model.addAttribute("topPostUrlsByViewCount", topPostUrlsByViewCount);
        model.addAttribute("topUsersByPostCount", topUsersByPostCount);
        model.addAttribute("totalPost", posts.size());
        model.addAttribute("totalComments", comments.size());
        model.addAttribute("totalVisitor", totalVisitor);
        model.addAttribute("posts", posts);
        return "marketing/dashboard";
    }

    @GetMapping("/chart")
    public String chart(Model model) throws JsonProcessingException {
        Map<String, Long> postsCountByDay = postService.getPostsCountByDayInCurrentWeek();
        model.addAttribute("postsCountByDay", postsCountByDay);
        Map<String, Long> postsCountByWeek = postService.getPostsCountByWeekInCurrentMonth();
        model.addAttribute("postsCountByWeek", postsCountByWeek);
        Map<String, Long> postsCountByMonth = postService.getPostsCountByMonthInCurrentYear();
        model.addAttribute("postsCountByMonth", postsCountByMonth);
        return "marketing/chart";
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
                             RedirectAttributes redirectAttributes,
                             Model model) throws IOException {
        // Check for validation errors
        if (result.hasErrors()) {
            model.addAttribute("post", postDto);
            return "marketing/create";
        }

        // Get raw content from the post DTO
        String rawContent = postDto.getContent();

        // Sanitize content to remove potentially unsafe elements but keep <a> tags
        Safelist safelist = Safelist.basicWithImages().addTags("a"); // Keep <a> tags
        String safeContent = Jsoup.clean(rawContent, safelist);

        // Parse the sanitized content
        Document doc = Jsoup.parseBodyFragment(safeContent);

        // Add a class to all <a> tags
        for (Element link : doc.select("a")) {
            link.addClass("riskLink");
        }

        // Get the modified HTML with new class on <a> tags
        String modifiedContent = doc.body().html();

        // Update the post DTO with modified content
        postDto.setContent(modifiedContent);

        // Generate URL and save the post
        postDto.setUrl(getUrl(postDto.getTitle()));
        postService.createPost(postDto, thumbnail);

        // Add flash attribute for success message
        redirectAttributes.addFlashAttribute("successMessage", "Post created successfully!");


        // Redirect to dashboard
        return "redirect:/posts/dashboard";
    }


    @GetMapping("/{postId}/edit")
    public String editPostForm(@PathVariable Long postId, Model model) {
        PostDto postDto = postService.findPostById(postId);
        model.addAttribute("post", postDto);
        return "marketing/edit";
    }

    @PostMapping("/{postId}")
    public String updatePost(@Valid @ModelAttribute("post") PostDto postDto,
                             @RequestParam("thumbnail") MultipartFile thumbnail,
                             @PathVariable("postId") Long postId,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("post", postDto);
            return "marketing/edit";
        }
        // Get raw content from the post DTO
        String rawContent = postDto.getContent();

        // Sanitize content to remove potentially unsafe elements but keep <a> tags
        Safelist safelist = Safelist.basicWithImages().addTags("a"); // Keep <a> tags
        String safeContent = Jsoup.clean(rawContent, safelist);

        // Parse the sanitized content
        Document doc = Jsoup.parseBodyFragment(safeContent);

        // Add a class to all <a> tags
        for (Element link : doc.select("a")) {
            link.addClass("riskLink");
        }

        // Get the modified HTML with new class on <a> tags
        String modifiedContent = doc.body().html();

        // Update the post DTO with modified content
        postDto.setContent(modifiedContent);
        postDto.setId(postId);
        postService.updatePost(postDto, thumbnail);
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

        int counter = visitorCountService.getVisitorCount(postUrl);
        model.addAttribute("counter", counter);

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

    @GetMapping("/search")
    public String searchPosts(@RequestParam(value = "query") String query, Model model){
        List<PostDto> posts = postService.searchPosts(query);
        model.addAttribute("posts", posts);
        return  "marketing/dashboard";
    }

    @GetMapping("/comments")
    public String postComments(Model model){
        List<CommentDto> comments = commentService.findAllComments();
        model.addAttribute("comments",comments);
        return  "marketing/comments";
    }

    @GetMapping("/comments/hide/{commentId}")
    public String hideComment(@PathVariable Long commentId){
        commentService.hideComment(commentId);
        Long postId = commentService.findPostIdByCommentId(commentId);
        return "redirect:/posts/"+ postId +"/comments";
    }

    @GetMapping("/comments/display/{commentId}")
    public String displayComment(@PathVariable Long commentId){
        commentService.displayComment(commentId);
        Long postId = commentService.findPostIdByCommentId(commentId);
        return "redirect:/posts/"+ postId +"/comments";
    }

}
