package com.example.bookshopwebapplication.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
@Data
@ToString
@NoArgsConstructor
public class ProductReview {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer ratingScore;
    private String content;
    private Integer isShow;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private User user;
    private Product product;

    public ProductReview(Long id,
                         Long userId,
                         Long productId,
                         Integer ratingScore,
                         String content,
                         Integer isShow,
                         Timestamp createdAt,
                         Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.ratingScore = ratingScore;
        this.content = content;
        this.isShow = isShow;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
