package com.theplayer.kits.bookstore.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.theplayer.kits.bookstore.dao.CategoryDao;
import com.theplayer.kits.bookstore.dao.ProductDao;
import com.theplayer.kits.bookstore.entity.Product;
import com.theplayer.kits.bookstore.exceptions.BadRequestException;
import com.theplayer.kits.bookstore.exceptions.ResourceNotFoundException;

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
        if (!StringUtils.hasText(product.getName())) {
            throw new BadRequestException("Product name cannot be empty!");
        }

        if (product.getCategory() == null) {
            throw new BadRequestException("Product category cannot be empty!");
        }

        if (!StringUtils.hasText(product.getCategory().getId())) {
            throw new BadRequestException("Product Category-ID cannot be empty!");
        }

        categoryDao.findById(product.getCategory().getId())
            .orElseThrow(() -> new BadRequestException(
                "Category with id: " + product.getCategory().getId() + " is not exist in database!"));

        product.setId(UUID.randomUUID().toString());
        return productDao.save(product);
    }

    public Product edit(Product product) {
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
