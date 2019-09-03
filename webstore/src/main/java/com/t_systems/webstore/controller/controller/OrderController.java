package com.t_systems.webstore.controller.controller;

import com.t_systems.webstore.model.dto.OrderDto;
import com.t_systems.webstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/orders")
    public String getOrdersPage(Model model, Principal principal){
        List<OrderDto> orders = orderService.getOrderDtosByUser(principal.getName());
        model.addAttribute("orders", orders);
        return "clientOrders";
    }
}
