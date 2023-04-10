package com.sber.java13.ecoteam.controller;

import com.sber.java13.ecoteam.dto.OrderDTO;
import com.sber.java13.ecoteam.service.OrderService;
import com.sber.java13.ecoteam.service.PointService;
import com.sber.java13.ecoteam.service.UserService;
import com.sber.java13.ecoteam.service.WasteService;
import com.sber.java13.ecoteam.service.userdetails.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final WasteService wasteService;
    private final PointService pointService;
    private final UserService userService;
    
    public OrderController(OrderService orderService, WasteService wasteService, PointService pointService, UserService userService) {
        this.orderService = orderService;
        this.wasteService = wasteService;
        this.pointService = pointService;
        this.userService = userService;
    }
    
    @GetMapping("/fromWaste/{wasteId}")
    public String orderFromWaste(@PathVariable Long wasteId, Model model) {
        model.addAttribute("waste", wasteService.getOne(wasteId));
        model.addAttribute("points", wasteService.getWasteWithPoints(wasteId).getPoints());
        return "userOrders/newOrder";
    }
    
    @PostMapping("/fromWaste")
    public String orderFromWaste(@ModelAttribute("orderWasteForm") OrderDTO orderDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        orderDTO.setUserId(Long.valueOf(customUserDetails.getUserId()));
        orderService.orderWaste(orderDTO);
        return "redirect:/order/user-orders/" + customUserDetails.getUserId();
    }
    
    @GetMapping("/user-orders/{id}")
    public String userWastes(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "5") int pageSize,
                             @PathVariable Long id,
                             Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<OrderDTO> orderPage = orderService.listUserOrders(id, pageRequest);
        model.addAttribute("orders", orderPage);
        model.addAttribute("wasteService", wasteService);
        model.addAttribute("pointService", pointService);
        model.addAttribute("userService", userService);
        return "userOrders/viewAllUserOrders";
    }
    
    @GetMapping("/agent-orders/{id}")
    public String agentOrders(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @PathVariable Long id,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<OrderDTO> orderPage = orderService.listAgentOrders(id, pageRequest);
        model.addAttribute("orders", orderPage);
        model.addAttribute("wasteService", wasteService);
        model.addAttribute("pointService", pointService);
        model.addAttribute("userService", userService);
        return "userOrders/viewAllAgentOrders";
    }
    
    @GetMapping("/fromPoint/{pointId}")
    public String orderFromPoint(@PathVariable Long pointId, Model model) {
        model.addAttribute("point", pointService.getOne(pointId));
        model.addAttribute("wastes", pointService.getPointWithWastes(pointId).getWastes());
        return "userOrders/newOrderPoint";
    }
    
    @PostMapping("/fromPoint")
    public String orderFromPoint(@ModelAttribute("orderPointForm") OrderDTO orderDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        orderDTO.setUserId(Long.valueOf(customUserDetails.getUserId()));
        orderService.orderWaste(orderDTO);
        return "redirect:/order/user-orders/" + customUserDetails.getUserId();
    }
    
    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable Long id) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        orderService.cancelOrder(id);
        if (userService.getOne(Long.valueOf(customUserDetails.getUserId())).getRole().getId().equals(Long.valueOf(3L))) {
            return "redirect:/order/agent-orders/" + customUserDetails.getUserId();
        } else {
            return "redirect:/order/user-orders/" + customUserDetails.getUserId();
        }
    }
    
    @GetMapping("/accept-order/{id}")
    public String acceptOrder(@PathVariable Long id) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        orderService.acceptOrder(id);
        return "redirect:/order/agent-orders/" + customUserDetails.getUserId();
    }
    
    @GetMapping("/complete-order/{id}")
    public String completeOrder(@PathVariable Long id) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        orderService.completeOrder(id);
        return "redirect:/order/agent-orders/" + customUserDetails.getUserId();
    }
}
