package com.kamikaze.bookstore.hung.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamikaze.bookstore.hung.entity.Category;

public interface CategoryDao extends JpaRepository<Category, String> {
    
}
