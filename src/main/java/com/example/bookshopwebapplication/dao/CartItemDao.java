package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.ICartItemDao;
import com.example.bookshopwebapplication.dao.mapper.CartItemMapper;
import com.example.bookshopwebapplication.entities.Cart;
import com.example.bookshopwebapplication.entities.CartItem;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CartItemDao extends AbstractDao<CartItem> implements ICartItemDao {
    public Long save(CartItem cartItem) {
        // set new Query
        clearSQL();
        builderSQL.append(
                "INSERT INTO cart_item (cartId, productId, quantity, createdAt) " +
                        "VALUES(?, ?, ?, ?)"
        );
        return insert(builderSQL.toString(), cartItem.getCartId(), cartItem.getProductId(),
                cartItem.getQuantity(), new Timestamp(System.currentTimeMillis())
                );
    }

    public void update(CartItem cartItem) {
        clearSQL();
        builderSQL.append(
                "UPDATE cart_item SET cartId = ?, productId = ?, quantity = ?, " +
                        "createdAt = ?, updatedAt = ? WHERE id = ?"
        );
        update(builderSQL.toString(), cartItem.getCartId(), cartItem.getProductId(),
                cartItem.getQuantity(), cartItem.getCreatedAt(), new Timestamp(System.currentTimeMillis()),
                cartItem.getId());
    }

    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM cart_item WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    public Optional<CartItem> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM cart_item WHERE id = ?");
        return Optional.ofNullable(query(builderSQL.toString(), new CartItemMapper(), id).get(0));
    }

    public List<CartItem> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM cart LIMIT " + offset + ", " + limit);
        List<CartItem> cartItems = query(builderSQL.toString(), new CartItemMapper());
        return cartItems.isEmpty() ? new LinkedList<>() : cartItems;
    }

    //int limit, int offset, String orderBy, String sort
    public List<CartItem> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM cart ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        List<CartItem> cartItems = query(builderSQL.toString(), new CartItemMapper());
        return cartItems.isEmpty() ? new LinkedList<>() : cartItems;
    }

    public int count() {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM cart_item"
        );
        return count(builderSQL.toString());
    }

    @Override
    public List<CartItem> getByCartId(long cartId) {
        clearSQL();
        builderSQL.append(
                "SELECT ci.*, p.name product_name, p.price product_price, p.discount product_discount, " +
                        "p.quantity product_quantity, p.imageName product_imageName " +
                        "FROM cart_item ci " +
                        "JOIN product p on p.id = ci.productId " +
                        "WHERE cartId = ? " +
                        "ORDER BY createdAt DESC"
        );
        List<CartItem> cartItems = query(builderSQL.toString(), new CartItemMapper(), cartId);
        return cartItems.isEmpty() ? new LinkedList<>() : cartItems;
    }

    @Override
    public Optional<CartItem> getByCartIdAndProductId(long cartId, long productId) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM cart_item WHERE cartId = ? AND productId = ?"
        );
        List<CartItem> cartItems = query(builderSQL.toString(), new CartItemMapper(), cartId, productId);
        return cartItems.isEmpty() ? Optional.empty() : Optional.ofNullable(cartItems.get(0));
    }

    @Override
    public int sumQuantityByUserId(long userId) {
        clearSQL();
        builderSQL.append(
                "SELECT SUM(ci.quantity) " +
                        "FROM cart_item ci " +
                        "JOIN cart c on c.id = ci.cartId " +
                        "WHERE c.userId = ?;"
        );
        return count(builderSQL.toString(), userId);
    }
}
