package com.tedu.sp04.order.service;

import com.tedu.sp01.pojo.Item;
import com.tedu.sp01.pojo.Order;
import com.tedu.sp01.pojo.User;
import com.tedu.sp01.service.OrderService;
import com.tedu.sp04.order.feignclient.ItemFeignService;
import com.tedu.sp04.order.feignclient.UserFeignService;
import com.tedu.web.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemFeignService itemService;
    @Autowired
    private UserFeignService userService;
    @Override
    public Order getOrder(String orderId) {
        //TODO: 调用user-service获取用户信息
        JsonResult<User> user = userService.getUser(7);
        //TODO: 调用item-service获取商品信息
        JsonResult<List<Item>> items = itemService.getItems(orderId);
        Order order = new Order();
        order.setId(orderId);
        order.setItems(items.getData());
        order.setUser(user.getData());
        return order;
    }

    @Override
    public void addOrder(Order order) {
        //TODO: 调用item-service减少商品库存
        itemService.decreaseNumber(order.getItems());
        //TODO: 调用user-service增加用户积分
        userService.addScore(7,100);
        log.info("保存订单："+order);
    }

}