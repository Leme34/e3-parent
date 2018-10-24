package com.lee.e3mall.order.service;

import com.Lee.dto.E3Result;
import com.lee.e3mall.order.pojo.OrderInfo;

public interface OrderService {

	E3Result createOrder(OrderInfo orderInfo);
}
