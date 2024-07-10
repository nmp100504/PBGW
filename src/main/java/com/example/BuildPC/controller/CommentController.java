package com.example.BuildPC.controller;

import com.example.BuildPC.dto.CommentDto;

import com.example.BuildPC.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postUrl}/comments")
    public String createComment(@PathVariable("postUrl") String postUrl,
                                @ModelAttribute("comment") CommentDto commentDto,
                                Model model){
        commentService.createComment(postUrl, commentDto);
        return "redirect:/blog/" + postUrl;
    }
}
