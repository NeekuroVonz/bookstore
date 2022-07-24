package com.theplayer.kits.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theplayer.kits.bookstore.entity.Orders;

public interface OrdersDao extends JpaRepository<Orders, String> {
    
}
