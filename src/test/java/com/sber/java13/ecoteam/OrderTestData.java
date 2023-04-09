package com.sber.java13.ecoteam;

import com.sber.java13.ecoteam.dto.OrderDTO;
import com.sber.java13.ecoteam.model.Order;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface OrderTestData {
    OrderDTO ORDER_DTO_1 = new OrderDTO(1L, "onDate1", false, false, 1, 1L, 1L, false);
    OrderDTO ORDER_DTO_2 = new OrderDTO(2L, "onDate2", false, false, 2, 2L, 2L, false);
    OrderDTO ORDER_DTO_3 = new OrderDTO(3L, "onDate3", false, false, 3, 3L, 3L, true);
    List<OrderDTO> ORDER_DTO_LIST = Arrays.asList(ORDER_DTO_1, ORDER_DTO_2, ORDER_DTO_3);
    Order ORDER_1 = new Order(null, LocalDate.now(), false, false, null, 1, null);
    Order ORDER_2 = new Order(null, LocalDate.now(), false, false, null, 2, null);
    Order ORDER_3 = new Order(null, LocalDate.now(), false, false, null, 3, null);
    
    List<Order> ORDER_LIST = Arrays.asList(ORDER_1, ORDER_2, ORDER_3);
}
