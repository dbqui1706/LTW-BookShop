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

    // Phương thức để lấy đối tượng CategoryDto dựa trên id
    @Override
    public Optional<CategoryDto> getById(Long id) {
        Optional<Category> category = categoryDao.getById(id);
        if (category.isPresent()) {
            // Chuyển đổi từ Category sang CategoryDto
            CategoryDto categoryDto = tCategory.toDto(category.get());

            // Lấy danh sách sản phẩm thuộc loại này từ ProductService và gán vào CategoryDto
            categoryDto.setProducts(ProductService.getInstance()
                    .getProductsByCategoryId(categoryDto.getId()));

            return Optional.of(categoryDto);
        }
        return Optional.empty();
    }

    // Phương thức để chèn một đối tượng CategoryDto mới
    @Override
    public Optional<CategoryDto> insert(CategoryDto categoryDto) {
        Long id = categoryDao.save(tCategory.toEntity(categoryDto));
        return getById(id);
    }

    // Phương thức để cập nhật thông tin của một đối tượng CategoryDto
    @Override
    public Optional<CategoryDto> update(CategoryDto categoryDto) {
        categoryDao.update(tCategory.toEntity(categoryDto));
        return getById(categoryDto.getId());
    }

    // Phương thức để xóa các đối tượng Category theo danh sách các id
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) categoryDao.delete(id);
    }

    // Phương thức để lấy một phần của danh sách đối tượng CategoryDto
    @Override
    public List<CategoryDto> getPart(Integer limit, Integer offset) {
        return categoryDao.getPart(limit, offset)
                .stream()
                .map(category -> getById(category.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Phương thức để lấy một phần của danh sách đối tượng CategoryDto và sắp xếp theo thứ tự
    @Override
    public List<CategoryDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return categoryDao.getOrderedPart(limit, offset, orderBy, sort)
                .stream()
                .map(category -> getById(category.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Phương thức để lấy danh sách tất cả các loại
    public List<Category> getAll() {
        return categoryDao.findAll();
    }

    // Phương thức để lấy đối tượng CategoryDto dựa trên id sản phẩm
    @Override
    public Optional<CategoryDto> getByProductId(long id) {
        Optional<Category> category = categoryDao.getByProductId(id);
        if (category.isPresent()) {
            // Chuyển đổi từ Category sang CategoryDto
            return Optional.of(tCategory.toDto(category.get()));
        }
        return Optional.empty();
    }
}
