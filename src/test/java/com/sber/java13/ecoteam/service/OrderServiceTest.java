package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.OrderTestData;
import com.sber.java13.ecoteam.dto.OrderDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.OrderMapper;
import com.sber.java13.ecoteam.model.Order;
import com.sber.java13.ecoteam.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class OrderServiceTest extends GenericTest<Order, OrderDTO> {
    
    public OrderServiceTest() {
        super();
        WasteService wasteService = Mockito.mock(WasteService.class);
        PointService pointService = Mockito.mock(PointService.class);
        UserService userService = Mockito.mock(UserService.class);
        repository = Mockito.mock(OrderRepository.class);
        mapper = Mockito.mock(OrderMapper.class);
        service = new OrderService((OrderRepository) repository, (OrderMapper) mapper, wasteService, pointService, userService);
    }
    
    @Test
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(OrderTestData.ORDER_LIST);
        Mockito.when(mapper.toDTOs(OrderTestData.ORDER_LIST)).thenReturn(OrderTestData.ORDER_DTO_LIST);
        List<OrderDTO> orderDTOS = service.listAll();
        log.info("Testing getAll(): " + orderDTOS);
        assertEquals(OrderTestData.ORDER_LIST.size(), orderDTOS.size());
    }
    
    @Test
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(OrderTestData.ORDER_1));
        Mockito.when(mapper.toDTO(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_DTO_1);
        OrderDTO orderDTO = service.getOne(1L);
        log.info("Testing getOne(): " + orderDTO);
        assertEquals(OrderTestData.ORDER_DTO_1, orderDTO);
    }
    
    @Test
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(OrderTestData.ORDER_DTO_1)).thenReturn(OrderTestData.ORDER_1);
        Mockito.when(mapper.toDTO(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_DTO_1);
        Mockito.when(repository.save(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_1);
        OrderDTO authorDTO = service.create(OrderTestData.ORDER_DTO_1);
        log.info("Testing create(): " + authorDTO);
        assertEquals(OrderTestData.ORDER_DTO_1, authorDTO);
    }
    
    @Test
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(OrderTestData.ORDER_DTO_1)).thenReturn(OrderTestData.ORDER_1);
        Mockito.when(mapper.toDTO(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_DTO_1);
        Mockito.when(repository.save(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_1);
        OrderDTO authorDTO = service.update(OrderTestData.ORDER_DTO_1);
        log.info("Testing update(): " + authorDTO);
        assertEquals(OrderTestData.ORDER_DTO_1, authorDTO);
    }
    
    @Test
    @Override
    protected void delete() throws MyDeleteException {
        Mockito.when(((OrderRepository) repository).checkOrderForDeletion(1L)).thenReturn(true);
//        Mockito.when(authorRepository.checkAuthorForDeletion(2L)).thenReturn(false);
        Mockito.when(repository.save(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_1);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(OrderTestData.ORDER_1));
        log.info("Testing delete() before: " + OrderTestData.ORDER_1.isDeleted());
        service.delete(1L);
        log.info("Testing delete() after: " + OrderTestData.ORDER_1.isDeleted());
        assertTrue(OrderTestData.ORDER_1.isDeleted());
    }
    
    @Test
    @Override
    protected void restore() {
        OrderTestData.ORDER_3.setDeleted(true);
        Mockito.when(repository.save(OrderTestData.ORDER_3)).thenReturn(OrderTestData.ORDER_3);
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(OrderTestData.ORDER_3));
        log.info("Testing restore() before: " + OrderTestData.ORDER_3.isDeleted());
        ((OrderService) service).restore(3L);
        log.info("Testing restore() after: " + OrderTestData.ORDER_3.isDeleted());
        assertFalse(OrderTestData.ORDER_3.isDeleted());
    }
    
    @Test
    @Override
    protected void getAllNotDeleted() {
        OrderTestData.ORDER_3.setDeleted(true);
        List<Order> orders = OrderTestData.ORDER_LIST.stream().filter(Predicate.not(Order::isDeleted)).toList();
        Mockito.when(repository.findAllByIsDeletedFalse()).thenReturn(orders);
        Mockito.when(mapper.toDTOs(orders)).thenReturn(
                OrderTestData.ORDER_DTO_LIST.stream().filter(Predicate.not(OrderDTO::isDeleted)).toList());
        List<OrderDTO> orderDTOS = service.listAllNotDeleted();
        log.info("Testing getAllNotDeleted(): " + orderDTOS);
        assertTrue(orders.size() != orderDTOS.size());
    }
    
    @Test
    public void listUserOrders() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Mockito.when(((OrderRepository) repository).getOrderByUserId(1L, pageRequest))
                .thenReturn(new PageImpl<>(OrderTestData.ORDER_LIST));
        Mockito.when(mapper.toDTOs(OrderTestData.ORDER_LIST)).thenReturn(OrderTestData.ORDER_DTO_LIST);
        Page<OrderDTO> orderDTOList = ((OrderService) service).listUserOrders(1L, pageRequest);
        log.info("Testing listUserOrders(): " + orderDTOList);
        assertEquals(OrderTestData.ORDER_DTO_LIST, orderDTOList.getContent());
    }
}
