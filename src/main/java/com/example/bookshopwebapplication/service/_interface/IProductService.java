package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.entities.Product;

import java.util.List;

public interface IProductService extends IService<ProductDto> {
    int countByCategoryId(long categoryId);

    public String getFirst(String twopartString);

    public String getLast(String twopartString);

    public String filterByPublishers(List<String> publishers);

    public String filterByPriceRanges(List<String> priceRanges);

    public String createFiltersQuery(List<String> filters);

    List<ProductDto> getOrderedPartByCategoryId(int productsPerPage, int offset, String orderBy, String sort, Long id);

    List<ProductDto> getOrderedPartByCategoryIdAndFilters(int productsPerPage, int offset, String orderBy, String sort, Long id, String filtersQuery);

    List<String> getPublishersByCategoryId(Long id);

    List<ProductDto> getRandomPartByCategoryId(int limit, int offset, Long id);
    public List<ProductDto> getProductsByCategoryId(Long categoryId);
}
