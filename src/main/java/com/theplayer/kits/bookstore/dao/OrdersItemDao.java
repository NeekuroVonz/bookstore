package com.theplayer.kits.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theplayer.kits.bookstore.entity.OrdersItem;

public interface OrdersItemDao extends JpaRepository<OrdersItem, String> {
    
}
