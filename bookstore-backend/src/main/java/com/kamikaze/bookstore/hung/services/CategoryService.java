package com.kamikaze.bookstore.hung.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamikaze.bookstore.hung.dao.CategoryDao;
import com.kamikaze.bookstore.hung.entity.Category;
import com.kamikaze.bookstore.hung.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryDao categoryDao;

    public Category findById(String id) {
        return categoryDao.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found!"));
    }

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public Category create(Category category) {
        category.setId(UUID.randomUUID().toString());
        return categoryDao.save(category);
    }

    public Category edit(Category category) {
        return categoryDao.save(category);
    }

    public void deleteById(String id) {
        categoryDao.deleteById(id);
    }

}
