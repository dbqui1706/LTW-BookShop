package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.CartDao;
import com.example.bookshopwebapplication.dao.CartItemDao;
import com.example.bookshopwebapplication.dto.CartDto;
import com.example.bookshopwebapplication.entities.Cart;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.service._interface.ICartService;
import com.example.bookshopwebapplication.service.transferObject.TCart;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartService implements ICartService {
    private CartDao cartDao = new CartDao();
    private TCart tCart = new TCart();
    private static final CartService instance = new CartService();

    public static CartService getInstance() {
        return instance;
    }

    public Optional<CartDto> insert(CartDto cartDto) {
        Long id = cartDao.save(tCart.toEntity(cartDto));
        return getById(id);
    }

    @Override
    public Optional<CartDto> update(CartDto cartDto) {
        cartDao.update(tCart.toEntity(cartDto));
        return getById(cartDto.getId());
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) cartDao.delete(id);
    }

    @Override
    public Optional<CartDto> getById(Long id) {
        Optional<Cart> cart = cartDao.getById(id);
        if (cart.isPresent()) {
            Cart currentCart = cart.get();
            CartDto cartDto = tCart.toDto(currentCart);
            cartDto.setUser(UserService.getInstance()
                    .getById(currentCart.getUserId()).get());
            cartDto.setCartItems(CartItemService.getInstance()
                    .getByCartId(currentCart.getId()));
            return Optional.ofNullable(cartDto);
        }
        return Optional.empty();
    }

    @Override
    public List<CartDto> getPart(Integer limit, Integer offset) {
        return cartDao.getPart(limit, offset)
                .stream()
                .map(cart -> getById(cart.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return cartDao.getOrderedPart(limit, offset, orderBy, sort)
                .stream()
                .map(cart -> getById(cart.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartDto> getByUserId(long userId) {
        Optional<Cart> cart = cartDao.getByUserId(userId);
        if (cart.isPresent()){
            return getById(cart.get().getId());
        }
        return Optional.empty();
    }

    @Override
    public int countCartItemQuantityByUserId(long userId) {
        return cartDao.countCartItemQuantityByUserId(userId);
    }

    @Override
    public int countOrderByUserId(long userId) {
        return cartDao.countOrderByUserId(userId);
    }

    @Override
    public int countOrderDeliverByUserId(long userId) {
        return cartDao.countOrderDeliverByUserId(userId);
    }

    @Override
    public int countOrderReceivedByUserId(long userId) {
        return cartDao.countOrderReceivedByUserId(userId);
    }
}