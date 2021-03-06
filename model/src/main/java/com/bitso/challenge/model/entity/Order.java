package com.bitso.challenge.model.entity;

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

    private Long id;
    private Long userId;
    private Status status;
    private Date created;
    private Currency major;
    private Currency minor;
    private BigDecimal amount;
    private BigDecimal price;
    private Boolean buy;
}
