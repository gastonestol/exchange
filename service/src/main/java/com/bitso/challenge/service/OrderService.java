package com.bitso.challenge.service;

import com.bitso.challenge.model.OrderModel;
import com.bitso.challenge.model.UserModel;
import com.bitso.challenge.model.entity.Currency;
import com.bitso.challenge.model.entity.Order;
import com.bitso.challenge.security.model.UserPrincipal;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Inject
    private OrderModel orderModel;

    public Long submit(Order order,UserPrincipal userPrincipal){
        if(order.getUserId().equals(userPrincipal.getId())) {
            return orderModel.submit(order);
        }else {
            throw new BadCredentialsException("Order userId do not match with logged user Id");
        }
    }
    public Optional<Order> get(Long id, UserPrincipal userPrincipal){
        return orderModel.get(id).map( order -> {
            if(!order.getUserId().equals(userPrincipal.getId())){
                order.setUserId(null);
            }
            return order;
        });
    }

    public List<Order> findOrdersForUser(Long userId, Order.Status status, Currency major, Currency minor, UserPrincipal userPrincipal){
        if(userId.equals(userPrincipal.getId())) {
            return orderModel.findOrdersForUser(userId, status, major, minor);
        }else{
            return Collections.emptyList();
        }

    }
    public List<Order> findOrdersForBook(Currency major, Currency minor){
        return orderModel.findOrdersForBook(major,minor).stream().peek(order -> order.setUserId(null)).collect(Collectors.toList());
    }

}
