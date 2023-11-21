package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IProductReviewDao;
import com.example.bookshopwebapplication.dao.mapper.ProductReviewMapper;
import com.example.bookshopwebapplication.entities.ProductReview;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ProductReviewDao extends AbstractDao<ProductReview> implements IProductReviewDao {

    public Long save(ProductReview pr) {
        // set new Query
        clearSQL();
        builderSQL.append(
                "INSERT INTO product_review (userId, productId, ratingScore, content, isShow" +
                        " createdAt, updatedAt) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)"
        );
        return insert(builderSQL.toString(), pr.getUserId(), pr.getProductId(), pr.getRatingScore(),
                pr.getContent(), pr.getIsShow(), pr.getCreatedAt(), new Timestamp(System.currentTimeMillis()));
    }

    public void update(ProductReview pr) {
        clearSQL();
        builderSQL.append(
                "UPDATE product_review SET ratingScore = ?, content = ?, " +
                        "updatedAt = " + new Timestamp(System.currentTimeMillis()) + " "
                        + "WHERE id = ?"
        );
        update(builderSQL.toString(), pr.getRatingScore(), pr.getContent(), pr.getUpdatedAt(), pr.getId());
    }

    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM product_review WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    public Optional<ProductReview> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM product_review WHERE id = ?");
        List<ProductReview> list = query(builderSQL.toString(), new ProductReviewMapper(), id);
        return list.isEmpty() ? null: Optional.ofNullable(list.get(0));
    }

    public List<ProductReview> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM product_review LIMIT " + offset + ", " + limit);
        return super.getPart(builderSQL.toString(), new ProductReviewMapper());
    }

    //int limit, int offset, String orderBy, String sort
    public List<ProductReview> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM product_review ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return super.getOrderedPart(builderSQL.toString(), new ProductReviewMapper());
    }

    public int count() {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM product_review"
        );
        return count(builderSQL.toString());
    }

    @Override
    public List<ProductReview> getOrderedPartByProductId(int limit, int offset, String orderBy, String sort, long productId) {
        clearSQL();
        builderSQL.append(
                "SELECT pr.*, u.fullname " +
                        "FROM product_review pr " +
                        "JOIN user u ON pr.userId = u.id " +
                        "WHERE productId = ? " +
                        "ORDER BY " + orderBy + " " + sort +
                        " LIMIT " + offset + ", " + limit
        );
        List<ProductReview> productReviews = query(builderSQL.toString(), new ProductReviewMapper(), productId);
        return productReviews.isEmpty() ? new LinkedList<>() : productReviews;
    }

    @Override
    public int countByProductId(long productId) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(id) FROM product_review WHERE productId = ?"
        );
        return count(builderSQL.toString(), productId);
    }

    @Override
    public int sumRatingScoresByProductId(long productId) {
        clearSQL();
        builderSQL.append(
                "SELECT SUM(ratingScore) FROM product_review WHERE productId = ?"
        );
        return count(builderSQL.toString(), productId);
    }

    @Override
    public void hide(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE product_review SET isShow = 0, " +
                        "updatedAt = " + new Timestamp(System.currentTimeMillis()) + " "
                        + "WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }

    @Override
    public void show(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE product_review SET isShow = 1, " +
                        "updatedAt = " + new Timestamp(System.currentTimeMillis()) + " "
                        + "WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }
}
