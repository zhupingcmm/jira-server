package com.mf.jira.server.service;

import com.mf.jira.server.dto.OrderDto;

public interface OrderService {
    void createOrder(OrderDto orderDto);
}
