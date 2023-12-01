package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IWishlistItemDao;
import com.example.bookshopwebapplication.dao.mapper.WishlistItemMapper;
import com.example.bookshopwebapplication.entities.WishListItem;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class WishlistItemDao extends AbstractDao<WishListItem> implements IWishlistItemDao {
    public Long save(WishListItem wishListItem) {
        // set new Query
        clearSQL();
        builderSQL.append(
                "INSERT INTO wishlist_item (userId, productId, createdAt) " +
                        "VALUES(?, ?, ?)"
        );
        return insert(builderSQL.toString(), wishListItem.getUserId(),
                wishListItem.getProductId(), new Timestamp(System.currentTimeMillis()));
    }

    public void update(WishListItem wishListItem) {
        clearSQL();
        builderSQL.append(
                "UPDATE wishlist_item SET userId = ?," +
                        " productId = ?, createdAt = " + new Timestamp(System.currentTimeMillis()) +
                        " WHERE id = ?"
        );
        update(builderSQL.toString(), wishListItem.getUserId(),
                wishListItem.getProductId());
    }

    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM wishlist_item WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    public Optional<WishListItem> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM wishlist_item WHERE id = ?");
        List<WishListItem> wishListItems = query(builderSQL.toString(), new WishlistItemMapper(), id);
        return wishListItems.isEmpty() ? Optional.empty() : Optional.ofNullable(wishListItems.get(0));
    }

    public List<WishListItem> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM wishlist_item LIMIT " + offset + ", " + limit);
        return super.getPart(builderSQL.toString(), new WishlistItemMapper());
    }

    //int limit, int offset, String orderBy, String sort
    public List<WishListItem> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM wishlist_item ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return super.getOrderedPart(builderSQL.toString(), new WishlistItemMapper());
    }

    public int count() {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM wishlist_item"
        );
        return count(builderSQL.toString());
    }

    @Override
    public List<WishListItem> getByUserId(long userId) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM wishlist_item WHERE userId = ?"
        );
        List<WishListItem> wishListItems = query(builderSQL.toString(), new WishlistItemMapper(), userId);
        return wishListItems.isEmpty() ? new LinkedList<>() : wishListItems;
    }

    @Override
    public List<WishListItem> getOrderedPartByUserId(long userId, Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM wishlist_item " +
                        "WHERE userId = ? " +
                        "ORDER BY " + orderBy + " " + sort + " " +
                        "LIMIT " + offset + ", " + limit + " "
        );
        List<WishListItem> wishListItems = query(builderSQL.toString(), new WishlistItemMapper(), userId);
        return wishListItems.isEmpty() ? new LinkedList<>() : wishListItems;
    }

    @Override
    public int countByUserIdAndProductId(Long userId, Long productId) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(id) " +
                        "FROM wishlist_item " +
                        "WHERE userId = ? AND productId = ?"
        );
        return count(builderSQL.toString(), userId, productId);
    }
}
