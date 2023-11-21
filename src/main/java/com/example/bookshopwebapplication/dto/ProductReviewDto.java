package com.example.bookshopwebapplication.dto;

import com.example.bookshopwebapplication.entities.Product;
import com.example.bookshopwebapplication.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
public class ProductReviewDto {
    private Long id;
    private Integer ratingScore;
    private String content;
    private Integer isShow;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private UserDto user;
    private ProductDto product;

    public ProductReviewDto(Long id,
                         Integer ratingScore,
                         String content,
                         Integer isShow,
                         Timestamp createdAt,
                         Timestamp updatedAt) {
        this.id = id;
        this.ratingScore = ratingScore;
        this.content = content;
        this.isShow = isShow;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
