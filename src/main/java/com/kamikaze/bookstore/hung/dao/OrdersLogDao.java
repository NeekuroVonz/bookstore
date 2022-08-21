package com.kamikaze.bookstore.hung.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamikaze.bookstore.hung.entity.OrdersLog;

public interface OrdersLogDao extends JpaRepository<OrdersLog, String> {
    
}
