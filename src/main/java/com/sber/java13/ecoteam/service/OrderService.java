package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.constants.Errors;
import com.sber.java13.ecoteam.dto.OrderDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.OrderMapper;
import com.sber.java13.ecoteam.model.Order;
import com.sber.java13.ecoteam.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService extends GenericService<Order, OrderDTO> {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        super(orderRepository, orderMapper);
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }
    
    public Page<OrderDTO> getAllOrdersWithUsers(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<OrderDTO> result = orderMapper.toDTOs(orderPage.getContent());
        return new PageImpl<>(result, pageable, orderPage.getTotalElements());
    }
    
    public Page<OrderDTO> getAllNotDeletedOrdersWithUsers(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAllByIsDeletedFalse(pageable);
        List<OrderDTO> result = orderMapper.toDTOs(orderPage.getContent());
        return new PageImpl<>(result, pageable, orderPage.getTotalElements());
    }
    
    public OrderDTO getOrderWithUsers(Long id) {
        return orderMapper.toDTO(mapper.toEntity(super.getOne(id)));
    }
    
    public OrderDTO create(final OrderDTO orderDTO) {
        orderDTO.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        orderDTO.setCreatedWhen(LocalDateTime.now());
        return mapper.toDTO(orderRepository.save(mapper.toEntity(orderDTO)));
    }
    
    public OrderDTO update(final OrderDTO orderDTO) {
        return mapper.toDTO(orderRepository.save(mapper.toEntity(orderDTO)));
    }
    
    public void delete(Long id) throws MyDeleteException {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Заказа с заданным id=" + id + " не существует!"));
        boolean orderCanBeDeleted = orderRepository.checkOrderForDeletion(id);
        if (orderCanBeDeleted) {
            markAsDeleted(order);
            orderRepository.save(order);
        }
        else {
            throw new MyDeleteException(Errors.Orders.ORDER_DELETE_ERROR);
        }
    }
    
    public void restore(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Заказа с заданным id=\" + id + \" не существует!"));
        unMarkAsDeleted(order);
        orderRepository.save(order);
    }
}
