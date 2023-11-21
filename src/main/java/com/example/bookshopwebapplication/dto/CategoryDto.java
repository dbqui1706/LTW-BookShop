package com.example.bookshopwebapplication.dto;

import com.example.bookshopwebapplication.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@ToString
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String imageName;
    private List<ProductDto> products;

    public CategoryDto(Long id,
                       String name,
                       String description,
                       String imageName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        products = new ArrayList<>();
    }
}
