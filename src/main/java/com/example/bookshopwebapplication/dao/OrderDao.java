package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IOrderDao;
import com.example.bookshopwebapplication.dao.mapper.OrderMapper;
import com.example.bookshopwebapplication.entities.Order;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OrderDao extends AbstractDao<Order> implements IOrderDao {
    public Long save(Order order){
        clearSQL();
        builderSQL.append(
                "INSERT INTO " +
                        "orders (userId, status, deliveryMethod," +
                        " deliveryPrice, createdAt)" +
                        " VALUES (?, ?, ?, ?, ?)"
        );
        return insert(builderSQL.toString(), order.getUserId(), order.getStatus(),
                order.getDeliveryMethod(), order.getDeliveryPrice(), order.getCreatedAt());
    }
    public void update(Order order) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders " +
                        "SET userId = ?, status = ?," +
                        " deliveryMethod = ?, " +
                        "deliveryPrice = ?, createdAt = ?," +
                        " updatedAt = ?" +
                        " WHERE id = ?"
        );
        update(builderSQL.toString(), order.getUserId(), order.getStatus(),
                order.getDeliveryMethod(), order.getDeliveryPrice(),
                order.getCreatedAt(), new Timestamp(System.currentTimeMillis()),
                order.getId());
    }

    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM orders WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    public Optional<Order> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM orders WHERE id = ?");
        return Optional.ofNullable(query(builderSQL.toString(), new OrderMapper(), id).get(0));
    }

    public List<Order> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM orders LIMIT " + offset + ", " + limit);
        List<Order> orders = query(builderSQL.toString(), new OrderMapper());
        return orders.isEmpty() ? new LinkedList<>() : orders;
    }

    //int limit, int offset, String orderBy, String sort
    public List<Order> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM orders ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + " ");
        List<Order> orders = query(builderSQL.toString(), new OrderMapper());
        return orders.isEmpty() ? new LinkedList<>() : orders;
    }
//    public Long getUserId(){
//        clearSQL();
//        builderSQL.append(
//        );
//    }
    @Override
    public List<Order> getOrderedPartByUserId(long userId, int limit, int offset) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM orders " +
                        "WHERE userId = ? " +
                        "ORDER BY orders.createdAt DESC " +
                        "LIMIT " + offset + ", " + limit
        );
        List<Order> orders = query(builderSQL.toString(), new OrderMapper(), userId);
        return orders.isEmpty() ? new LinkedList<>(): orders;
    }

    @Override
    public int countByUserId(long userId) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(orders.id) FROM orders WHERE userId = ?"
        );
        return count(builderSQL.toString(), userId);
    }

    @Override
    public void cancelOrder(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }

    @Override
    public int count() {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(id) FROM orders"
        );
        return count(builderSQL.toString());
    }

    @Override
    public void confirm(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders SET status = 2, updatedAt = NOW() WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }

    @Override
    public void cancel(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }

    @Override
    public void reset(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders SET status = 1, updatedAt = NOW() WHERE id = :id"
        );
        update(builderSQL.toString(), id);
    }
}
