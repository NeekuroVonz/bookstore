package com.kamikaze.bookstore.hung.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamikaze.bookstore.hung.entity.OrdersItem;

public interface OrdersItemDao extends JpaRepository<OrdersItem, String> {
    
}
