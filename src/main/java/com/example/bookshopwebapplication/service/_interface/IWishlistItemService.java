package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.WishlistItemDto;
import com.example.bookshopwebapplication.entities.WishListItem;

import java.util.List;

public interface IWishlistItemService extends IService<WishlistItemDto> {
    List<WishlistItemDto> getByUserId(long userId);
    int countByUserIdAndProductId(Long userId, Long id);
}
