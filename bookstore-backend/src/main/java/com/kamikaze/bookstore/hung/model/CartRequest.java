package com.kamikaze.bookstore.hung.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class CartRequest implements Serializable {
    private String productId;
    private Double quantity;
}
