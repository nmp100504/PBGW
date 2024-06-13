//package com.example.BuildPC.service;
//
//import com.example.BuildPC.model.Post;
//import com.example.BuildPC.repository.PostRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PostService {
//
//    @Autowired
//    private PostRepository postRepository;
//
//    public List<Post> findAll() {
//        return postRepository.findAll();
//    }
//
//    public Post findById(Integer id) {
//        return postRepository.findById(id).orElse(null);
//    }
//
//    public Post save(Post post) {
//        return postRepository.save(post);
//    }
//
//    public void deleteById(Integer id) {
//        postRepository.deleteById(id);
//    }
//}
