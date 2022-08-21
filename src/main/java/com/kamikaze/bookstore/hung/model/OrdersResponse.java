package com.kamikaze.bookstore.hung.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kamikaze.bookstore.hung.entity.Orders;
import com.kamikaze.bookstore.hung.entity.OrdersItem;

import lombok.Data;

@Data
public class OrdersResponse implements Serializable {
    private String id;
    private String orderNumber;
    private Date date;
    private String customerName;
    private String shippingAddress;
    private Date orderTime;
    private BigDecimal amount;
    private BigDecimal shipping;
    private BigDecimal total;
    private List<OrdersResponse.Item> items;

    public OrdersResponse(Orders orders, List<OrdersItem> ordersItems) {
        this.id = orders.getId();
        this.orderNumber = orders.getNumber();
        this.date = orders.getDate();
        this.customerName = orders.getUsers().getName();
        this.shippingAddress = orders.getShippingAddress();
        this.orderTime = orders.getOrderTime();
        this.amount = orders.getAmount();
        this.shipping = orders.getShipping();
        this.total = orders.getTotal();
        items = new ArrayList<>();
        for (OrdersItem ordersItem : ordersItems) {
            Item item = new Item();
            item.setProductId(ordersItem.getProduct().getId());
            item.setProductName(ordersItem.getProduct().getName());
            item.setQuantity(ordersItem.getQuantity());
            item.setPrice(ordersItem.getPrice());
            item.setAmount(ordersItem.getAmount());
            items.add(item);
        }
    }

    @Data
    public static class Item implements Serializable {
        private String productId;
        private String productName;
        private Double quantity;
        private BigDecimal price;
        private BigDecimal amount;
    }
}
