package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.OrderItemDao;
import com.example.bookshopwebapplication.dto.OrderItemDto;
import com.example.bookshopwebapplication.entities.OrderItem;
import com.example.bookshopwebapplication.service._interface.IOrderItemService;
import com.example.bookshopwebapplication.service.transferObject.TCartItem;
import com.example.bookshopwebapplication.service.transferObject.TOrderItem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderItemService implements IOrderItemService {
    private OrderItemDao orderItemDao = new OrderItemDao();
    private TOrderItem tOrderItem = new TOrderItem();
    @Override
    public void bulkInsert(List<OrderItemDto> orderItemDtoList) {
        for (OrderItemDto orderItemDto: orderItemDtoList){
            this.insert(orderItemDto);
        }
    }

    @Override
    public List<String> getProductNamesByOrderId(long orderId) {
        return orderItemDao.getProductNamesByOrderId(orderId);
    }

    @Override
    public List<OrderItemDto> getByOrderId(long orderId) {
        return orderItemDao.getByOrderId(orderId)
                .stream()
                .map(orderItem -> getById(orderItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<OrderItemDto> insert(OrderItemDto orderItemDto) {
        Long id = orderItemDao.save(tOrderItem.toEntity(orderItemDto));
        return getById(id);
    }

    @Override
    public Optional<OrderItemDto> update(OrderItemDto orderItemDto) {
        orderItemDao.update(tOrderItem.toEntity(orderItemDto));
        return getById(orderItemDto.getId());
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id: ids) orderItemDao.delete(id);
    }

    @Override
    public Optional<OrderItemDto> getById(Long id) {
        Optional<OrderItem> orderItem = orderItemDao.getById(id);
        if (orderItem.isPresent()){
            OrderItemDto orderItemDto = tOrderItem.toDto(orderItem.get());
            orderItemDto.setOrder(OrderService.getInstance()
                    .getById(orderItem.get().getOrderId()).get());
            orderItemDto.setProduct(ProductService.getInstance()
                    .getById(orderItem.get().getProductId()).get());

            return Optional.of(orderItemDto);
        }
        return Optional.empty();
    }

    @Override
    public List<OrderItemDto> getPart(Integer limit, Integer offset) {
        return orderItemDao.getPart(limit, offset) .stream()
                .map(orderItem -> getById(orderItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return orderItemDao.getOrderedPart(limit, offset, orderBy, sort)
                .stream()
                .map(orderItem -> getById(orderItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
