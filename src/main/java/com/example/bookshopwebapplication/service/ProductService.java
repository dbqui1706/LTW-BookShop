package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.CategoryDao;
import com.example.bookshopwebapplication.dao.ProductDao;
import com.example.bookshopwebapplication.dao._interface.ICategoryDao;
import com.example.bookshopwebapplication.dao._interface.IProductDao;
import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.entities.Product;
import com.example.bookshopwebapplication.service._interface.IProductService;
import com.example.bookshopwebapplication.service.transferObject.TProduct;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductService implements IProductService {
    private ProductDao productDao = new ProductDao();
    private TProduct tProduct = new TProduct();
    private static final ProductService instance = new ProductService();

    public static ProductService getInstance() {
        return instance;
    }

    @Override
    public Optional<ProductDto> insert(ProductDto productDto) {
        Long id = productDao.save(tProduct.toEntity(productDto));
        return getById(id);
    }

    @Override
    public Optional<ProductDto> update(ProductDto productDto) {
        productDao.update(tProduct.toEntity(productDto));
        return getById(productDto.getId());
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            productDao.delete(id);
        }
    }

    @Override
    public Optional<ProductDto> getById(Long id) {
        Optional<Product> product = productDao.getById(id);
        if (product.isPresent()) return Optional.of(tProduct.toDto(product.get()));
        return Optional.empty();
    }

    @Override
    public List<ProductDto> getPart(Integer limit, Integer offset) {
        return productDao.getPart(limit, offset)
                .stream()
                .map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return productDao.getOrderedPart(limit, offset, orderBy, sort)
                .stream()
                .map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return productDao.count();
    }

    @Override
    public int countByCategoryId(long categoryId) {
        return productDao.countByCategoryId(categoryId);
    }

    @Override
    public List<ProductDto> getOrderedPartByCategoryId(int limit, int offset, String orderBy, String sort, Long id) {
        return productDao.getOrderedPartByCategoryId(limit, offset, orderBy, sort, id)
                .stream()
                .map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getOrderedPartByCategoryIdAndFilters(int limit, int offset, String orderBy, String sort, Long id, String filtersQuery) {
        return productDao.getOrderedPartByCategoryIdAndFilters(limit, offset, orderBy, sort, id, filtersQuery)
                .stream()
                .map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPublishersByCategoryId(Long categoryId) {
        return productDao.getPublishersByCategoryId(categoryId);
    }

    @Override
    public List<ProductDto> getRandomPartByCategoryId(int limit, int offset, Long categoryId) {
        return productDao.getRandomPartByCategoryId(limit, offset, categoryId)
                .stream().map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByCategoryId(Long categoryId) {
        return productDao.getProductsByCategoryId(categoryId)
                .stream().map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public int countByQuery(String query) {
        return productDao.countByQuery(query);
    }

    @Override
    public List<ProductDto> getByQuery(String query, int limit, int offset) {
        return productDao.getByQuery(query, limit, offset)
                .stream()
                .map(product -> getById(product.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());
    }

    @Override
    public String getFirst(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[0] : "";
    }

    @Override
    public String getLast(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[1] : "";
    }

    private int getMinPrice(String priceRange) {
        return Integer.parseInt(getFirst(priceRange));
    }

    private int getMaxPrice(String priceRange) {
        String maxPriceString = getLast(priceRange);
        if (maxPriceString.equals("infinity")) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(maxPriceString);
    }

    @Override
    public String filterByPublishers(List<String> publishers) {
        String publishersString = publishers.stream().map(p -> "'" + p + "'").collect(Collectors.joining(", "));
        return "p.publisher IN (" + publishersString + ")";
    }

    @Override
    public String filterByPriceRanges(List<String> priceRanges) {
        String priceRangeConditions = priceRanges.stream().map(
                priceRange -> "p.price BETWEEN " + getMinPrice(priceRange) + " AND " + getMaxPrice(priceRange)
        ).collect(Collectors.joining(" OR "));
        return "(" + priceRangeConditions + ")";
    }

    @Override
    public String createFiltersQuery(List<String> filters) {
        return String.join(" AND ", filters);
    }

    public int countByCategoryIdAndFilters(Long id, String filtersQuery) {
        return productDao.countByCategoryIdAndFilters(id, filtersQuery);
    }
}
