package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.ICategoryDao;
import com.example.bookshopwebapplication.dao.mapper.CategoryMapper;
import com.example.bookshopwebapplication.dao.mapper.ProductMapper;
import com.example.bookshopwebapplication.entities.Category;
import com.example.bookshopwebapplication.entities.Product;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CategoryDao extends AbstractDao<Category> implements ICategoryDao {
    public Long save(Category category) {
        // set new Query
        clearSQL();
        builderSQL.append("INSERT INTO category (name, description, imageName) ");
        builderSQL.append("VALUES (?, ?, ?)");
        return insert(builderSQL.toString(), category.getName(), category.getDescription(),
                category.getImageName());
    }

    public void update(Category category) {
        clearSQL();
        builderSQL.append("UPDATE category SET name = ?, description = ?, imageName = ? ");
        builderSQL.append("WHERE id = ?");
        update(builderSQL.toString(), category.getName(), category.getDescription(), category.getImageName(),
                category.getId());
    }

    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM category WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    @Override
    public List<Category> findAll() {
        clearSQL();
        builderSQL.append("SELECT * FROM category");
        return super.query(builderSQL.toString(), new CategoryMapper());
    }

    @Override
    public Optional<Category> getByProductId(long id) {
        clearSQL();
        builderSQL.append(
                "SELECT c.* " +
                        "FROM product_category pc " +
                        "JOIN category c ON pc.categoryId = c.id " +
                        "WHERE productId = ?"
        );
        List<Category> categories = query(builderSQL.toString(), new CategoryMapper(), id);
        return categories.isEmpty() ? null : Optional.ofNullable(categories.get(0));
    }


    public List<Category> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM category LIMIT " + offset + ", " + limit);
        return query(builderSQL.toString(), new CategoryMapper());
    }

    //int limit, int offset, String orderBy, String sort
    public List<Category> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM category ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return query(builderSQL.toString(), new CategoryMapper());
    }
    public int count(){
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM category"
        );
        return count(builderSQL.toString());
    }
    public Optional<Category> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM category WHERE id = ?");
        List<Category> list = query(builderSQL.toString(), new CategoryMapper(), id);
        return list.isEmpty() ? null : Optional.ofNullable(list.get(0));
    }
}
