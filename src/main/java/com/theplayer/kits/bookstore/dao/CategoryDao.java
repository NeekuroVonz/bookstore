package com.theplayer.kits.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theplayer.kits.bookstore.entity.Category;

public interface CategoryDao extends JpaRepository<Category, String> {
    
}
