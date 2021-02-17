package com.bitso.challenge.controller;

import com.bitso.challenge.model.entity.Currency;
import com.bitso.challenge.model.entity.Order;
import com.bitso.challenge.security.model.UserPrincipal;
import com.bitso.challenge.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST endpoint to submit and query orders.
 */
@RestController("orders")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;


    @RequestMapping("/get/{id}")
    @ResponseBody
    public Optional<Order> get(@PathVariable Long id, Authentication authentication) {
        Optional<Order> order = orderService.get(id,(UserPrincipal) authentication.getPrincipal());
        order.ifPresentOrElse(
                o -> log.debug("get {}: {}", id, o),
                () -> log.debug("get {}: null", id));
        return order;
    }

    @RequestMapping("/submit") @PostMapping
    public Order submit(@RequestBody Order order, Authentication authentication) {
        log.debug("Submitting order {}", order);
        orderService.submit(order,(UserPrincipal) authentication.getPrincipal());
        return order;
    }

    @RequestMapping("/book/{major}/{minor}")
    public List<Order> findOrdersForBook(@PathVariable String major,
                                         @PathVariable String minor) {
        //TODO validate currencies
        var maj = Currency.valueOf(major);
        var min = Currency.valueOf(minor);
        return orderService.findOrdersForBook(maj, min);
    }

    @RequestMapping("/query/{userId}/{status}/{major}/{minor}")
    public List<Order> getBy(@PathVariable Long userId,
                             @PathVariable String status,
                             @PathVariable String major,
                             @PathVariable String minor,
                             Authentication authentication) {
        Order.Status st = orderStatus(status);
        Currency majorCurr = checkCurrency(major);
        Currency minorCurr = checkCurrency(minor);

        List<Order> r = orderService.findOrdersForUser(userId, st, majorCurr, minorCurr,(UserPrincipal) authentication.getPrincipal());
        log.debug("Query {}/{}/{}/{} returns {} orders", userId, st, majorCurr, minorCurr, r.size());
        return r;
    }

    private Currency checkCurrency(String currency){
        return currency == null || currency.isEmpty() ? null : Currency.valueOf(currency);
    }
    private Order.Status orderStatus(String status){
        return status == null || status.isEmpty() ? null : Order.Status.valueOf(status);
    }
}
