package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.mapper.PostMapper;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.repository.PostRepository;
import com.example.BuildPC.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final Path storageLocation = Paths.get("public/images/thumbnail/"); // Set your upload directory

    @Override
    public List<PostDto> findAllPost() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostMapper::mapToPostDTO).toList();
    }

    @Override
    public void createPost(PostDto postDto) {
//        if (postDto.getThumbnailFile() != null && !postDto.getThumbnailFile().isEmpty()) {
//            try {
//                String fileName = saveThumbnail(postDto.getThumbnailFile());
//                postDto.setThumbnail(fileName);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        Post post = PostMapper.mapToPost(postDto);
        postRepository.save(post);
    }

    @Override
    public PostDto findPostById(long id) {
        Post post = postRepository.findById(id).get();
        return PostMapper.mapToPostDTO(post);
    }

    @Override
    public void updatePost(PostDto postDto) {
//        if (postDto.getThumbnailFile() != null && !postDto.getThumbnailFile().isEmpty()) {
//            try {
//                String fileName = saveThumbnail(postDto.getThumbnailFile());
//                postDto.setThumbnail(fileName);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        Post post = PostMapper.mapToPost(postDto);
        postRepository.save(post);
    }

    @Override
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostDto findPostByUrl(String postUrl) {
        Post post = postRepository.findByUrl(postUrl).get();
        return PostMapper.mapToPostDTO(post);
    }

    @Override
    public List<PostDto> searchPosts(String query) {
        List<Post> posts = postRepository.searchPosts(query);
        return posts.stream().map(PostMapper::mapToPostDTO).collect(Collectors.toList());
    }

    private String saveThumbnail(MultipartFile file) throws IOException {
        Files.createDirectories(storageLocation);
        String fileName = file.getOriginalFilename();
        Path targetLocation = storageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation);
        return fileName;
    }
}
