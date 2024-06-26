//package com.example.BuildPC.controller;
//
//import com.example.BuildPC.model.Comment;
//import com.example.BuildPC.model.Product;
//import com.example.BuildPC.service.CommentService;
//import com.example.BuildPC.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//
//import java.util.List;
//
//@Controller
//public class CommentController {
//
//    @Autowired
//    private CommentService commentService;
//
//    @Autowired
//    private ProductService productService;
//
//    @GetMapping("/product/reviews")
//    public String showReviews(Model model) {
//        // Assuming a method to get the current product
//        Product product = getCurrentProduct();
//        model.addAttribute("comments", product.getComments());
//        model.addAttribute("averageRating", calculateAverageRating(product.getComments()));
//        return "reviews";
//    }
//
//    @PostMapping("/product/reviews")
//    public String addReview(
//            @RequestParam Integer productId,
//            @RequestParam String name,
//            @RequestParam String email,
//            @RequestParam String feedback,
//            @RequestParam int starRating,
//            Model model) {
//        Comment comment = new Comment();
//        comment.setName(name);
//        comment.setEmail(email);
//        comment.setFeedback(feedback);
//        comment.setStarRating(starRating);
//
//        Product product = productService.findById(productId);
//        comment.setProduct(product);
//
//        commentService.saveComment(comment);
//        return "redirect:/product/reviews";
//    }
//
//    private double calculateAverageRating(List<Comment> comments) {
//        return comments.stream().mapToInt(Comment::getStarRating).average().orElse(0.0);
//    }
//
////    private Product getCurrentProduct() {
////        // Implement method to get the current product, for example by ID from request or session
////    }
//}
