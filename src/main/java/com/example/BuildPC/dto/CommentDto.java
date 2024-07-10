package com.example.BuildPC.dto;

import com.example.BuildPC.model.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String name;
    private String email;
    private String content;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
