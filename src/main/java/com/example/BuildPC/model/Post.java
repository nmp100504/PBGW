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
    private String postTitle;

    @Column(name = "post_content",columnDefinition = "TEXT")

    private String postConent;

    @Column(name = "post_create_at")
    private Date postCreatedAt;

    @Column(name = "post_update_at")
    private Date postUpdateAt;

    @Column(name = "post_image")
    private String imageFileName;


    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Post(String postTitle, String postConent, Date postCreatedAt, Date postUpdateAt, User user, String imageFileName) {
        this.imageFileName = imageFileName;
        this.postTitle = postTitle;
        this.postConent = postConent;
        this.postCreatedAt = postCreatedAt;
        this.postUpdateAt = postUpdateAt;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostConent() {
        return postConent;
    }

    public void setPostConent(String postConent) {
        this.postConent = postConent;
    }

    public Date getPostCreatedAt() {
        return postCreatedAt;
    }

    public void setPostCreatedAt(Date postCreatedAt) {
        this.postCreatedAt = postCreatedAt;
    }

    public Date getPostUpdateAt() {
        return postUpdateAt;
    }

    public void setPostUpdateAt(Date postUpdateAt) {
        this.postUpdateAt = postUpdateAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
