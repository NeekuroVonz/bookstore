package com.theplayer.kits.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theplayer.kits.bookstore.entity.Cart;

public interface CartDao extends JpaRepository<Cart, String> {
    
}