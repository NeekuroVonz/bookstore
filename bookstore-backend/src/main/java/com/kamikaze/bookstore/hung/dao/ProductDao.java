package com.kamikaze.bookstore.hung.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamikaze.bookstore.hung.entity.Product;

public interface ProductDao extends JpaRepository<Product, String> {

    Product findByCategoryId(String id);
    
}
