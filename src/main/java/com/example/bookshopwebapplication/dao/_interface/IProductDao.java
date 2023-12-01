package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.entities.Product;

import java.util.List;

public interface IProductDao extends IGenericDao<Product>{
    int countByCategoryIdAndFilters(Long id, String filtersQuery);
    List<Product> getOrderedPartByCategoryId(int limit, int offset, String orderBy, String sort, Long id);

    List<Product> getOrderedPartByCategoryIdAndFilters(int limit, int offset, String orderBy, String sort, Long id, String filtersQuery);

    List<String> getPublishersByCategoryId(Long id);

    List<Product> getRandomPartByCategoryId(int limit, int offset, Long categoryId);
    List<Product> getProductsByCategoryId(Long id);
    int countByQuery(String query);
    List<Product> getByQuery(String query, int limit, int offset);
}
