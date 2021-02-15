package com.bitso.challenge.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents a buy or sell order.
 */
@Data
public class Order {

    public enum Status {
        active,
        processing,
        cancelled,
        complete,
    }

    private long id;
    private long userId;
    private Status status;
    private Date created;
    private Currency major;
    private Currency minor;
    private BigDecimal amount;
    private BigDecimal price;
    private boolean buy;
}
