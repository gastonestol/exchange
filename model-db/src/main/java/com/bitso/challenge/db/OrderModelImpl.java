package com.bitso.challenge.db;

import com.bitso.challenge.model.OrderModel;
import com.bitso.challenge.model.entity.Currency;
import com.bitso.challenge.model.entity.Order;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class OrderModelImpl implements OrderModel {

    private Sql2o sql2o;

    public OrderModelImpl(String connStr) {
        sql2o = new Sql2o(connStr,null,null);
        createTable();
    }

    @Override
    public long submit(Order order) {
        order.setCreated(new Date());
        order.setStatus(Order.Status.active);
        insert(order);
        return order.getId();
    }

    public long insert(Order order) {
        //TODO validate
        try (Connection conn = sql2o.open()) {
            Long id = conn.createQuery("insert into orders " +
                    "(user_id,status,created,major,minor,amount,price,buy) " +
                    "VALUES (:user_id,:status,:created,:major,:minor,:amount,:price,:buy)")
                    .addParameter("user_id", order.getUserId())
                    .addParameter("status", order.getStatus())
                    .addParameter("created", order.getCreated())
                    .addParameter("major", order.getMajor())
                    .addParameter("minor", order.getMinor())
                    .addParameter("amount", order.getAmount())
                    .addParameter("price", order.getPrice())
                    .addParameter("buy", order.getBuy())
                    .executeUpdate()
                    .getKey(Long.class);
            order.setId(id);
            return order.getId();
        }
    }

    @Override
    public Optional<Order> get(long id) {
        try (Connection conn = sql2o.open()) {
            Order order = conn.createQuery("select * from orders where id = :id")
                    .addParameter("id", id)
                    .addColumnMapping("user_id","userId")
                    .executeAndFetchFirst(Order.class);
            return Optional.ofNullable(order);
        }
    }

    @Override
    public List<Order> findOrdersForUser(long userId, Order.Status status, Currency major, Currency minor) {
        try (Connection conn = sql2o.open()) {
            List<Order> orders = conn.createQuery("select * from orders " +
                    "where user_id = :userId " +
                    "and (status is null or status = :status)" +
                    "and (major is null or major = :major)" +
                    "and (minor is null or minor = :minor)")
                    .addParameter("userId", userId)
                    .addParameter("status", status)
                    .addParameter("major", major)
                    .addParameter("minor", minor)
                    .addColumnMapping("user_id","userId")
                    .executeAndFetch(Order.class);
            return orders;
        }

    }

    @Override
    public List<Order> findOrdersForBook(Currency major, Currency minor) {
        try (Connection conn = sql2o.open()) {
            List<Order> orders = conn.createQuery("select * from orders " +
                    "where major = :major " +
                    "and minor = :minor")
                    .addParameter("major", major)
                    .addParameter("minor", minor)
                    .addColumnMapping("user_id","userId")
                    .executeAndFetch(Order.class);
            return orders;
        }
    }

    private void createTable() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("CREATE TABLE IF NOT EXISTS orders (" +
                    "   id INTEGER identity," +
                    "   user_id INTEGER NOT NULL," +
                    "   status varchar(20) NOT NULL," +
                    "   created datetime NOT NULL," +
                    "   major varchar(10) NOT NULL," +
                    "   minor varchar(10) NOT NULL," +
                    "   amount float NOT NULL," +
                    "   price float NOT NULL," +
                    "   buy NUMERIC" +
                    " )"
            ).executeUpdate();
        }
    }
}
