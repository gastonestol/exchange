package com.bitso.challenge.model.ram;

import com.bitso.challenge.entity.Currency;
import com.bitso.challenge.entity.Order;
import com.bitso.challenge.model.OrderModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * RAM-based implementation of orders.
 */
public class OrderModelImpl implements OrderModel {

    private HashMap<Long, Order> orders = new HashMap<>(100);
    private AtomicLong seq = new AtomicLong();

    @Override
    public long submit(Order order) {
        order.setCreated(new Date());
        order.setStatus(Order.Status.active);
        insert(order);
        return order.getId();
    }

    public long insert(Order order) {
        //TODO validate
        order.setId(seq.incrementAndGet());
        orders.put(order.getId(), order);
        return order.getId();
    }

    @Override
    public Optional<Order> get(long id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findOrdersForUser(long userId, Order.Status status, Currency currency) {
        return orders.values().stream()
            .filter(o -> o.getUserId() == userId)
            .filter(o -> status == null || status == o.getStatus())
            .filter(o -> currency == null || currency == o.getMajor() || currency == o.getMinor())
            .collect(Collectors.toList());
    }

    @Override
    public List<Order> findOrdersForBook(Currency major, Currency minor) {
        throw new UnsupportedOperationException("Implement me!");
    }
}
