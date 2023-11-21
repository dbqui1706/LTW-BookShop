package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.ProductReviewDao;
import com.example.bookshopwebapplication.dto.ProductReviewDto;
import com.example.bookshopwebapplication.entities.ProductReview;
import com.example.bookshopwebapplication.service._interface.IProductReviewService;
import com.example.bookshopwebapplication.service.transferObject.ITransfer;
import com.example.bookshopwebapplication.service.transferObject.TProductReview;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductReviewService implements IProductReviewService {
    private ProductReviewDao productReviewDao = new ProductReviewDao();
    private TProductReview tProductReview = new TProductReview();

    @Override
    public Optional<ProductReviewDto> insert(ProductReviewDto productReviewDto) {
        Long id = productReviewDao.save(tProductReview.toEntity(productReviewDto));
        return getById(id);
    }

    @Override
    public Optional<ProductReviewDto> update(ProductReviewDto productReview) {
        return Optional.empty();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) productReviewDao.delete(id);
    }

    @Override
    public Optional<ProductReviewDto> getById(Long id) {
        Optional<ProductReview> productReview = productReviewDao.getById(id);
        if (productReview.isPresent()) {
            ProductReviewDto productReviewDto = tProductReview.toDto(productReview.get());
            productReviewDto.setUser(UserService.getInstance()
                    .getById(productReview.get().getUserId()).get());
            productReviewDto.setProduct(ProductService.getInstance()
                    .getById(productReview.get().getProductId()).get());
            return Optional.of(productReviewDto);
        }
        return Optional.empty();
    }

    @Override
    public List<ProductReviewDto> getPart(Integer limit, Integer offset) {
        return productReviewDao.getPart(limit, offset)
                .stream()
                .map(productReview -> getById(productReview.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductReviewDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return productReviewDao.getOrderedPart(limit, offset, orderBy, sort)
                .stream()
                .map(productReview -> getById(productReview.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductReviewDto> getOrderedPartByProductId(int limit, int offset, String orderBy, String sort, long productId) {
        return productReviewDao.getOrderedPartByProductId(limit, offset, orderBy, sort, productId)
                .stream()
                .map(productReview -> getById(productReview.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public int countByProductId(long id) {
        return productReviewDao.countByProductId(id);
    }

    @Override
    public int sumRatingScoresByProductId(long id) {
        return productReviewDao.sumRatingScoresByProductId(id);
    }
}
