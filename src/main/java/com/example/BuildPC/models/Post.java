package com.example.BuildPC.models;


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

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "post_content")
    private String postConent;

    @Column(name = "post_create_at")
    private Date postCreatedAt;

    @Column(name = "post_update_at")
    private Date postUpdateAt;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Post(String postTitle, String postConent, Date postCreatedAt, Date postUpdateAt, User user) {
        this.postTitle = postTitle;
        this.postConent = postConent;
        this.postCreatedAt = postCreatedAt;
        this.postUpdateAt = postUpdateAt;
        this.user = user;
    }
}
