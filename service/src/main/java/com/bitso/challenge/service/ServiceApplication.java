package com.bitso.challenge.service;

import com.bitso.challenge.entity.Currency;
import com.bitso.challenge.entity.Order;
import com.bitso.challenge.entity.User;
import com.bitso.challenge.model.OrderModel;
import com.bitso.challenge.model.UserModel;
import com.bitso.challenge.model.ram.OrderModelImpl;
import com.bitso.challenge.model.ram.UserModelImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.LongStream;

/**
 * Entry point and configuration provider.
 */
@SpringBootApplication
public class ServiceApplication {

    @Bean
    public UserModel userModel() {
        UserModelImpl um = new UserModelImpl();
        //Populate
        LongStream.rangeClosed(1, 10).forEach(id -> {
            User u = new User();
            u.setId(id);
            u.setEmail("user" + id + "@bitso.com");
            u.setPassword("password" + id);
            um.add(u);
        });
        return um;
    }

    @Bean
    public OrderModel orderModel() throws IOException, URISyntaxException {
        OrderModelImpl om = new OrderModelImpl();
        //Populate
        Files.lines(Paths.get(getClass().getResource("/orders.csv").toURI())).map(line -> {
            Order order = new Order();
            String[] parts = line.split(",");
            order.setUserId(Long.parseLong(parts[0]));
            order.setStatus(Order.Status.valueOf(parts[1]));
            order.setCreated(new Date(Long.parseLong(parts[2])));
            order.setMajor(Currency.valueOf(parts[3]));
            order.setMinor(Currency.valueOf(parts[4]));
            order.setAmount(new BigDecimal(parts[5]));
            order.setPrice(new BigDecimal(parts[6]));
            order.setBuy("buy".equals(parts[7]));
            return order;
        }).forEach(om::insert);
        return om;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
