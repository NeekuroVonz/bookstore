package com.kamikaze.bookstore.hung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamikaze.bookstore.hung.entity.Category;
import com.kamikaze.bookstore.hung.services.CategoryService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// http://localhost:9999/bookstore/category

@RestController
@RequestMapping("/bookstore")
// @PreAuthorize("isAuthenticated()")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value="/category")
    public List<Category> findAll() {
        return categoryService.findAll();
    }
    
    @GetMapping(value="/category/{id}")
    public Category findById(@PathVariable("id") String id) {
        return categoryService.findById(id);
    }

    @PostMapping(value="/category")
    @PreAuthorize("hasAuthority('admin')")
    public Category create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @PutMapping(value="/category")
    @PreAuthorize("hasAuthority('admin')")
    public Category edit(@RequestBody Category category) {
        return categoryService.edit(category);
    }
    
    @DeleteMapping(value = "/category/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public void deleteById(@PathVariable("id") String id) {
        categoryService.deleteById(id);
    }

}
