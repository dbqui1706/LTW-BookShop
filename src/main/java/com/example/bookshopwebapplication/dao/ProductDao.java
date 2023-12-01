package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IProductDao;
import com.example.bookshopwebapplication.dao.mapper.ProductMapper;
import com.example.bookshopwebapplication.entities.Product;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ProductDao extends AbstractDao<Product> implements IProductDao {

    public Long save(Product product) {
        // set new Query
        clearSQL();
        builderSQL.append("INSERT INTO product (name, price, discount, quantity, totalBuy, author, ");
        builderSQL.append("pages, publisher, yearPublishing, description, imageName, shop, createdAt, ");
        builderSQL.append("updatedAt,  startsAt, endsAt) ");
        builderSQL.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        return insert(builderSQL.toString(), product.getName(), product.getPrice(), product.getDiscount(),
                product.getQuantity(), product.getTotalBuy(), product.getAuthor(), product.getPages(),
                product.getPublisher(), product.getYearPublishing(), product.getDescription(),
                product.getImageName(), product.getShop(), product.getCreatedAt(), product.getUpdatedAt(),
                product.getStartsAt(), product.getEndsAt());
    }

    public void update(Product product) {
        clearSQL();
        builderSQL.append("UPDATE product SET name = ?, price = ?, discount = ?, quantity = ?, totalBuy = ?, author = ?, ");
        builderSQL.append("pages = ?, publisher = ?, yearPublishing = ?, description = ?, imageName = ?, " +
                "shop = ?, createdAt = ?, ");
        builderSQL.append("updatedAt = ?,  startsAt = ?, endsAt = ?) ");
        builderSQL.append("WHERE id = ?");
        update(builderSQL.toString(), product.getName(), product.getPrice(), product.getDiscount(),
                product.getQuantity(), product.getTotalBuy(), product.getAuthor(), product.getPages(),
                product.getPublisher(), product.getYearPublishing(), product.getDescription(),
                product.getImageName(), product.getShop(), product.getCreatedAt(),
                new Timestamp(System.currentTimeMillis()), product.getStartsAt(),
                product.getEndsAt(), product.getId());
    }

    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM product WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    public Optional<Product> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM product WHERE id = ?");
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), id);
        return products.isEmpty() ? Optional.empty() : Optional.ofNullable(products.get(0));
    }

    public List<Product> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM product LIMIT " + offset + ", " + limit);
        return super.getPart(builderSQL.toString(), new ProductMapper());
    }

    //int limit, int offset, String orderBy, String sort
    public List<Product> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM product ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return super.getOrderedPart(builderSQL.toString(), new ProductMapper());
    }
    public int count(){
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM product"
        );
        return count(builderSQL.toString());
    }
    public int countByCategoryId(Long id) {
        clearSQL();
        builderSQL.append("SELECT COUNT(productId) FROM product_category WHERE categoryId = ?");
        return count(builderSQL.toString(), id);
    }

    @Override
    public int countByCategoryIdAndFilters(Long id, String filtersQuery) {
        clearSQL();
        builderSQL.append("SELECT COUNT(p.id) ");
        builderSQL.append("FROM product_category pc ");
        builderSQL.append("JOIN product p ON pc.productId = p.id ");
        builderSQL.append("WHERE pc.categoryId = ? ");
        builderSQL.append("AND " + filtersQuery);
        return count(builderSQL.toString(), id);
    }

    @Override
    public List<Product> getOrderedPartByCategoryId(int limit, int offset, String orderBy, String sort, Long id) {
        clearSQL();
        builderSQL.append("SELECT p.* FROM product_category pc JOIN product p ");
        builderSQL.append("ON pc.productId = p.id WHERE pc.categoryId = ? ");
        builderSQL.append("ORDER BY p." + orderBy + " " + sort + " ");
        builderSQL.append("LIMIT " + offset + ", " + limit);
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), id);
        return products.isEmpty() ? null : products;
    }

    @Override
    public List<Product> getOrderedPartByCategoryIdAndFilters(int limit, int offset, String orderBy, String sort, Long id, String filtersQuery) {
        clearSQL();
        builderSQL.append(
                "SELECT p.* " +
                        "FROM product_category pc " +
                        "JOIN product p ON pc.productId = p.id " +
                        "WHERE pc.categoryId = ? " +
                        "AND " + filtersQuery +
                        "ORDER BY p." + orderBy + " " + sort + " " +
                        "LIMIT " + offset + ", " + limit
        );
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), id);
        return products.isEmpty() ? new LinkedList<Product>() : products;
    }

    @Override
    public List<String> getPublishersByCategoryId(Long id) {
        clearSQL();
        builderSQL.append(
                "SELECT DISTINCT p.publisher " +
                        "FROM product_category pc " +
                        "JOIN product p ON pc.productId = p.id " +
                        "WHERE pc.categoryId = ? " +
                        "ORDER BY p.publisher"
        );
        try {
            connection = getConnection();
            List<String> result = new LinkedList<>();
            statement = connection.prepareStatement(builderSQL.toString());
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("publisher"));
            }
            return result;
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                return null;
            }
        }
    }

    @Override
    public List<Product> getRandomPartByCategoryId(int limit, int offset, Long categoryId) {
        clearSQL();
        builderSQL.append(
                "SELECT p.* FROM product_category pc " +
                        "JOIN product p ON pc.productId = p.id " +
                        "WHERE pc.categoryId = ? " +
                        "ORDER BY RAND() " +
                        "LIMIT " + offset + ", " + limit
        );
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), categoryId);
        return products.isEmpty() ? new LinkedList<>() : products;
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        clearSQL();
        builderSQL.append("SELECT p.* FROM product_category pc JOIN product p ");
        builderSQL.append("ON pc.productId = p.id WHERE pc.categoryId = ? ");
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), categoryId);
        return products.isEmpty() ? new LinkedList<Product>() : products;
    }

    @Override
    public int countByQuery(String query) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(id) " +
                        "FROM product " +
                        "WHERE name LIKE CONCAT('%', ?, '%')"
        );
        return count(builderSQL.toString(), query);
    }

    @Override
    public List<Product> getByQuery(String query, int limit, int offset) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM product " +
                        "WHERE name LIKE CONCAT('%', ?, '%') " +
                        "LIMIT " + limit + " OFFSET " + offset
        );
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), query);
        return products.isEmpty() ? new LinkedList<>() : products;
    }
}
