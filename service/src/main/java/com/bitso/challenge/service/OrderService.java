package com.bitso.challenge.service;

import com.bitso.challenge.model.OrderModel;
import com.bitso.challenge.model.UserModel;
import com.bitso.challenge.model.entity.Currency;
import com.bitso.challenge.model.entity.Order;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Inject
    private OrderModel orderModel;

    public long submit(Order order){
        return orderModel.submit(order);
    }
    public Optional<Order> get(long id){
        return orderModel.get(id);
    }

    public List<Order> findOrdersForUser(long userId, Order.Status status, Currency major, Currency minor){
        return orderModel.findOrdersForUser(userId,status,major,minor);

    }
    public List<Order> findOrdersForBook(Currency major, Currency minor){
        return orderModel.findOrdersForBook(major,minor);
    }

    public long insert(Order order){
        return orderModel.insert(order);
    }

}
