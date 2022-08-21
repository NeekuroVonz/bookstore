package com.kamikaze.bookstore.hung.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.kamikaze.bookstore.hung.model.OrdersStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class Orders implements Serializable {
    @Id
    private String id;
    private String number;
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn
    @ManyToOne
    private Users users;
    private String shippingAddress;
    private BigDecimal amount;
    private BigDecimal shipping;
    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    private OrdersStatus orderStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;
}
