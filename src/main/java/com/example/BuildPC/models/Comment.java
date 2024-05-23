package com.example.BuildPC.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commnent_id")
    private Integer id;

    @Column(name = "comment_content")
    private String commentContent;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Comment(String commentContent, User user, Product product) {
        this.commentContent = commentContent;
        this.user = user;
        this.product = product;
    }
}
