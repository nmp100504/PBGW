package com.example.BuildPC.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;

    @NotEmpty(message = "Title should not be empty")
    @Size(max = 255, message = "Title should not exceed 255 characters")
    private String title;

    private String url;

    @NotEmpty(message = "Content should not be empty")
    @Column(columnDefinition="TEXT")
    private String content;

    @NotEmpty(message = "Short description should not be empty")
    @Size(max = 500, message = "Short description should not exceed 500 characters")
    private String shortDescription;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    private UserDto createdBy;
    private Set<CommentDto> comments = new HashSet<>();

}
