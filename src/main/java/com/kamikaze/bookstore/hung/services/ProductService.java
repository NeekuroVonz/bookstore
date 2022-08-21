package com.kamikaze.bookstore.hung.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kamikaze.bookstore.hung.dao.CategoryDao;
import com.kamikaze.bookstore.hung.dao.ProductDao;
import com.kamikaze.bookstore.hung.entity.Product;
import com.kamikaze.bookstore.hung.exceptions.BadRequestException;
import com.kamikaze.bookstore.hung.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
    
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductDao productDao;

    public Product findById(String id) {
        return productDao.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found!"));
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }
    
    public Product create(Product product) {
        // check product name
        if (!StringUtils.hasText(product.getName())) {
            throw new BadRequestException("Product name cannot be empty!");
        }

        // check product category
        if (product.getCategory() == null) {
            throw new BadRequestException("Product category cannot be empty!");
        }

        // check product category-id
        if (!StringUtils.hasText(product.getCategory().getId())) {
            throw new BadRequestException("Product Category-ID cannot be empty!");
        }

        // check category-id in database
        categoryDao.findById(product.getCategory().getId())
            .orElseThrow(() -> new BadRequestException(
                "Category with id: " + product.getCategory().getId() + " is not exist in database!"));

        product.setId(UUID.randomUUID().toString());
        return productDao.save(product);
    }

    public Product edit(Product product) {
        // check product id
        if (!StringUtils.hasText(product.getId())) {
            throw new BadRequestException("Product ID must be filled!");
        }
        productDao.findById(product.getId())
            .orElseThrow(() -> new BadRequestException(
                "Product with id: " + product.getId() + " not found!"));

        // check product name
        if (!StringUtils.hasText(product.getName())) {
            throw new BadRequestException("Product name cannot be empty!");
        }

        // check category
        if (product.getCategory() == null) {
            throw new BadRequestException("Product category cannot be empty!");
        }

        // check null category id
        if (!StringUtils.hasText(product.getCategory().getId())) {
            throw new BadRequestException("Product Category-ID cannot be empty!");
        }

        // check category-id in database
        categoryDao.findById(product.getCategory().getId())
            .orElseThrow(() -> new BadRequestException(
                "Category with id: " + product.getCategory().getId() + " is not exist in database!"));

        return productDao.save(product);
    }

    public Product changeImage(String id, String image) {
        Product product = findById(id);
        product.setImage(image);
        return productDao.save(product);
    }

    public void deleteById(String id) {
        productDao.deleteById(id);
    }
}
