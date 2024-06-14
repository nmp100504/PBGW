package com.example.BuildPC.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;


    @Column(name = "post_title",columnDefinition = "TEXT")
    private String title;

    @Column(name = "post_content",columnDefinition = "TEXT")

    private String content;

    @Column(name = "post_create_at")
    private Date createdAt;

    @Column(name = "post_update_at")
    private Date updatedAt;

    @Column(name = "post_image")
    private String imageFileName;


    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Post(String postTitle, String postConent, Date postCreatedAt, Date postUpdateAt, User user, String imageFileName) {
        this.imageFileName = imageFileName;
        this.title = postTitle;
        this.content = postConent;
        this.createdAt = postCreatedAt;
        this.updatedAt = postUpdateAt;
        this.user = user;
    }


}
