package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.entities.WishListItem;

import java.util.List;

public interface IWishlistItemDao extends IGenericDao<WishListItem> {
    List<WishListItem> getByUserId(long userId);

    int countByUserIdAndProductId(Long userId, Long id);
}
