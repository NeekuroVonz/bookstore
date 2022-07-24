package com.theplayer.kits.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theplayer.kits.bookstore.entity.Product;

public interface ProductDao extends JpaRepository<Product, String> {
    
}
