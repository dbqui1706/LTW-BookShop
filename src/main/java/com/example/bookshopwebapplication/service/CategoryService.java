package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.CategoryDao;
import com.example.bookshopwebapplication.dao.ProductDao;
import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.entities.Category;
import com.example.bookshopwebapplication.service._interface.ICategoryService;
import com.example.bookshopwebapplication.service.transferObject.ITransfer;
import com.example.bookshopwebapplication.service.transferObject.TCategory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryService implements ICategoryService {
    private CategoryDao categoryDao = new CategoryDao();
    private TCategory tCategory = new TCategory();

    @Override
    public Optional<CategoryDto> getById(Long id) {
        Optional<Category> category = categoryDao.getById(id);
        if (category.isPresent()) {
            CategoryDto categoryDto = tCategory.toDto(category.get());
            categoryDto.setProducts(ProductService.getInstance()
                    .getProductsByCategoryId(categoryDto.getId()));
            return Optional.of(categoryDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<CategoryDto> insert(CategoryDto categoryDto) {
        Long id = categoryDao.save(tCategory.toEntity(categoryDto));
        return getById(id);
    }

    @Override
    public Optional<CategoryDto> update(CategoryDto categoryDto) {
        categoryDao.update(tCategory.toEntity(categoryDto));
        return getById(categoryDto.getId());
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) categoryDao.delete(id);
    }

    @Override
    public List<CategoryDto> getPart(Integer limit, Integer offset) {
        return categoryDao.getPart(limit, offset)
                .stream()
                .map(category -> getById(category.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return categoryDao.getOrderedPart(limit, offset, orderBy, sort)
                .stream()
                .map(category -> getById(category.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Category> getAll() {
        return categoryDao.findAll();
    }

    @Override
    public Optional<CategoryDto> getByProductId(long id) {
        Optional<Category> category = categoryDao.getByProductId(id);
        if (category.isPresent()) return Optional.of(tCategory.toDto(category.get()));
        return Optional.empty();
    }
}
