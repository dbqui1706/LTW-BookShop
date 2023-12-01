package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.CartItemDao;
import com.example.bookshopwebapplication.dto.CartDto;
import com.example.bookshopwebapplication.dto.CartItemDto;
import com.example.bookshopwebapplication.entities.Cart;
import com.example.bookshopwebapplication.entities.CartItem;
import com.example.bookshopwebapplication.service._interface.ICartItemService;
import com.example.bookshopwebapplication.service.transferObject.TCartItem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartItemService implements ICartItemService {
    private final CartItemDao cartItemDao = new CartItemDao();
    private TCartItem tCartItem = new TCartItem();
    private static final CartItemService instance = new CartItemService();
    public static CartItemService getInstance() {
        return instance;
    }
    @Override
    public List<CartItemDto> getByCartId(long cartId) {
        List<CartItem> cartItems =  cartItemDao.getByCartId(cartId);
        return cartItemDao.getByCartId(cartId)
                .stream()
                .map(cartItem -> getById(cartItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartItemDto> getByCartIdAndProductId(long cartId, long productId) {
        return getById(cartItemDao.getByCartIdAndProductId(cartId, productId).get().getId());
    }

    @Override
    public int sumQuantityByUserId(long userId) {
        return cartItemDao.sumQuantityByUserId(userId);
    }


    @Override
    public Optional<CartItemDto> insert(CartItemDto cartItemDto) {
        Long id = cartItemDao.save(tCartItem.toEntity(cartItemDto));
        return getById(id);
    }

    @Override
    public Optional<CartItemDto> update(CartItemDto cartItemDto) {
        cartItemDao.update(tCartItem.toEntity(cartItemDto));
        return getById(cartItemDto.getId());
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) cartItemDao.delete(id);
    }

    @Override
    public Optional<CartItemDto> getById(Long id) {
        Optional<CartItem> cartItem = cartItemDao.getById(id);
        if (cartItem.isPresent()){
            CartItemDto cartItemDto = tCartItem.toDto(cartItem.get());
//            cartItemDto.setCart(CartService.getInstance()
//                    .getById(cartItem.get().getCartId()).get());
            cartItemDto.setProduct(ProductService.getInstance()
                    .getById(cartItem.get().getProductId()).get());
            return Optional.of(cartItemDto);
        }
        return Optional.empty();
    }

    @Override
    public List<CartItemDto> getPart(Integer limit, Integer offset) {
        return cartItemDao.getPart(limit, offset)
                .stream()
                .map(cartItem -> getById(cartItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItemDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return cartItemDao.getOrderedPart(limit, offset, orderBy, sort).stream()
                .map(cartItem -> getById(cartItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return cartItemDao.count();
    }

}
