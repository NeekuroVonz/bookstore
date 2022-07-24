package com.theplayer.kits.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theplayer.kits.bookstore.entity.OrdersLog;

public interface OrdersLogDao extends JpaRepository<OrdersLog, String> {
    
}
