package com.kamikaze.bookstore.hung.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kamikaze.bookstore.hung.dao.CartDao;
import com.kamikaze.bookstore.hung.dao.ProductDao;
import com.kamikaze.bookstore.hung.entity.Cart;
import com.kamikaze.bookstore.hung.entity.Product;
import com.kamikaze.bookstore.hung.entity.Users;
import com.kamikaze.bookstore.hung.exceptions.BadRequestException;

@Service
public class CartService {
    
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CartDao cartDao;

    @Transactional
    public Cart addToCart(String username, String productId, Double quantity) {
        // Is the product in the database?
        // Is it already in the user's cart?
        // If it is then update the quantity and count
        // If it doesn't exist then create a new one

        Product product = productDao.findById(productId)
            .orElseThrow(() -> new BadRequestException("Product with id: " + productId + " not found!"));

        Optional<Cart> optional = cartDao.findByUsersIdAndProductId(username, productId);
        Cart cart;
        if (optional.isPresent()) {
            cart = optional.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setAmount(new BigDecimal(cart.getPrice().doubleValue() * cart.getQuantity()));
            cart.setTimeMade(new Date());
            cartDao.save(cart);
        } else {
            cart = new Cart();
            cart.setId(UUID.randomUUID().toString());
            cart.setProduct(product);
            cart.setQuantity(quantity);
            cart.setPrice(product.getPrice());
            cart.setAmount(new BigDecimal(cart.getPrice().doubleValue() * cart.getQuantity()));
            cart.setUsers(new Users(username));
            cart.setTimeMade(new Date());
            cartDao.save(cart);
        }

        return cart;

    }

    @Transactional
    public Cart updateQuantity(String username, String productId, Double quantity) {
        Date date = new Date();
        Cart cart = cartDao.findByUsersIdAndProductId(username, productId)
            .orElseThrow(() -> new BadRequestException("Product with id: " + productId + " not found in your cart!"));

        cart.setQuantity(cart.getQuantity() + quantity);
        cart.setAmount(new BigDecimal(cart.getPrice().doubleValue() * cart.getQuantity()));
        cart.setTimeMade(date);
        cartDao.save(cart);
        return cart;
    }

    @Transactional
    public void delete(String username, String productId) {
        Cart cart = cartDao.findByUsersIdAndProductId(username, productId)
            .orElseThrow(() -> new BadRequestException("Product with id: " + productId + " not found in your cart!"));
        
        cartDao.delete(cart);
    }

    public List<Cart> findByUsersId(String username) {
        return cartDao.findByUsersId(username);
    }

}
