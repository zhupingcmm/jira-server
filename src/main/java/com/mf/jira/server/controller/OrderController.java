package com.mf.jira.server.controller;

import com.mf.jira.server.base.BaseResponse;
import com.mf.jira.server.dto.OrderDto;
import com.mf.jira.server.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public BaseResponse addOrder(@RequestBody OrderDto orderDto){
        orderService.createOrder(orderDto);
        return BaseResponse.success();
    }
}
