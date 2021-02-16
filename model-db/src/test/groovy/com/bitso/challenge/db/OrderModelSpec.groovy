package com.bitso.challenge.db


import com.bitso.challenge.model.entity.Currency
import com.bitso.challenge.model.entity.Order
import spock.lang.Specification

/**
 * Test the in-memory implementation of the OrderModel.
 */

class OrderModelSpec extends Specification {

    def model = new OrderModelImpl("jdbc:hsqldb:mem:testDB")

    void "insert, then query"() {
        model.submit(new Order(userId: 1, major: 'btc', minor: 'mxn',
                amount:1.0, price: 350_000.00))
        model.submit(new Order(userId: 2, major: 'btc', minor: 'mxn',
                amount:2.0, price: 330_000.00))
        model.submit(new Order(userId: 3, major: 'eth', minor: 'mxn',
                amount:0.00001, price: 12_000.00))
        expect: "orders by user work"
            model.findOrdersForUser(1, Order.Status.active, Currency.btc, Currency.mxn).size() == 1
            model.findOrdersForUser(2, Order.Status.active, Currency.btc, Currency.mxn).size() == 1
            model.findOrdersForUser(3, Order.Status.active, Currency.eth, Currency.mxn).size() == 1
            model.findOrdersForUser(3, Order.Status.active, Currency.ltc, null).empty
            model.findOrdersForUser(3, Order.Status.complete, Currency.eth, null).empty
        and: "orders by book work"
            model.findOrdersForBook(Currency.btc, Currency.mxn).size() == 2
            model.findOrdersForBook(Currency.eth, Currency.mxn).size() == 1
            model.findOrdersForBook(Currency.ltc, Currency.mxn).empty
    }
}
