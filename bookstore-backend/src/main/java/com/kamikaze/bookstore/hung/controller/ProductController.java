package com.kamikaze.bookstore.hung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamikaze.bookstore.hung.entity.Product;
import com.kamikaze.bookstore.hung.services.ProductService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// http://localhost:9999/bookstore/product

@RestController
@RequestMapping("/bookstore")
@PreAuthorize("isAuthenticated()")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @GetMapping(value="/product")
    public List<Product> findAll() {
        return productService.findAll();
    }
    
    @GetMapping(value="/product/{id}")
    public Product findById(@PathVariable("id") String id) {
        return productService.findById(id);
    }

    @PostMapping(value="/product")
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping(value="/product")
    public Product edit(@RequestBody Product product) {
        return productService.edit(product);
    }
    
    @DeleteMapping(value = "/product/{id}")
    public void deleteById(@PathVariable("id") String id) {
        productService.deleteById(id);
    }

}
