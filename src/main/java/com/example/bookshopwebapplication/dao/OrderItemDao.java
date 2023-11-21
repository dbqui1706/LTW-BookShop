package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IOrderItemDao;
import com.example.bookshopwebapplication.dao.mapper.OrderItemMapper;
import com.example.bookshopwebapplication.entities.OrderItem;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OrderItemDao extends AbstractDao<OrderItem> implements IOrderItemDao {
    public Long save(OrderItem orderItem) {
        clearSQL();
        builderSQL.append(
                "INSERT INTO " +
                        "order_item (orderId, productId, price, discount, quantity, " +
                        "createdAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        );
        return insert(builderSQL.toString(), orderItem.getOrderId(), orderItem.getProductId(),
                orderItem.getPrice(), orderItem.getDiscount(), orderItem.getQuantity(),
                orderItem.getCreatedAt());
    }

    public void update(OrderItem orderItem) {
        clearSQL();
        builderSQL.append(
                "UPDATE order_item " +
                        "SET orderId = ?, productId = ?, " +
                        "price = ?, discount = ?, " +
                        "quantity = ?, createdAt = ?, " +
                        "updatedAt = ? " +
                        "WHERE id = ?"
        );
        update(builderSQL.toString(), orderItem.getOrderId(), orderItem.getProductId(),
                orderItem.getPrice(), orderItem.getDiscount(), orderItem.getQuantity(),
                orderItem.getCreatedAt(), orderItem.getCreatedAt(), orderItem.getId());
    }

    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM order_item WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    public Optional<OrderItem> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM order_item WHERE id = ?");
        return Optional.ofNullable(query(builderSQL.toString(), new OrderItemMapper(), id).get(0));
    }

    public List<OrderItem> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM order_item LIMIT " + offset + ", " + limit);
        List<OrderItem> orderItems = query(builderSQL.toString(), new OrderItemMapper());
        return orderItems.isEmpty() ? new LinkedList<>() : orderItems;
    }

    //int limit, int offset, String orderBy, String sort
    public List<OrderItem> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM order_item ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + " ");
        List<OrderItem> orderItems = query(builderSQL.toString(), new OrderItemMapper());
        return orderItems.isEmpty() ? new LinkedList<>() : orderItems;
    }

    @Override
    public void bulkInsert(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) this.save(orderItem);
    }

    @Override
    public List<String> getProductNamesByOrderId(long orderId) {
        try {
            clearSQL();
            builderSQL.append(
                    "SELECT name " +
                            "FROM product p " +
                            "JOIN order_item o ON p.id = o.productId " +
                            "WHERE o.orderId = ?"
            );
            List<String> result = new LinkedList<>();
            connection = getConnection();
            statement = connection.prepareStatement(builderSQL.toString());
            statement.setLong(1, orderId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("name"));
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
    public List<OrderItem> getByOrderId(long orderId) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM order_item WHERE orderId = ?"
        );
        List<OrderItem> orderItems = query(builderSQL.toString(), new OrderItemMapper(), orderId);
        return orderItems.isEmpty() ? new LinkedList<>() : orderItems;
    }
}
