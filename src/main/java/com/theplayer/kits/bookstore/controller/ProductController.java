package com.theplayer.kits.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.theplayer.kits.bookstore.entity.Product;
import com.theplayer.kits.bookstore.services.ProductService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// http://localhost:9999/bookstore/product

@RestController
@RequestMapping("/bookstore")
public class ProductController {
    
    @Autowired
    private ProductService ProductService;

    @GetMapping(value="/product")
    public List<Product> findAll() {
        return ProductService.findAll();
    }
    
    @GetMapping(value="/product/{id}")
    public Product findById(@PathVariable("id") String id) {
        return ProductService.findById(id);
    }

    @PostMapping(value="/product")
    public Product create(@RequestBody Product Product) {
        return ProductService.create(Product);
    }

    @PutMapping(value="/product")
    public Product edit(@RequestBody Product Product) {
        return ProductService.edit(Product);
    }
    
    @DeleteMapping(value = "/product/{id}")
    public void deleteById(@PathVariable("id") String id) {
        ProductService.deleteById(id);
    }

}
