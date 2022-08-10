package com.kamikaze.bookstore.hung.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrdersRequest implements Serializable {
    private BigDecimal shipping;
    private String shippingAddress;
    private List<CartRequest> items;
}
