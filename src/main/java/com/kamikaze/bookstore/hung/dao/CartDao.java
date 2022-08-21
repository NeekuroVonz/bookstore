package com.kamikaze.bookstore.hung.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamikaze.bookstore.hung.entity.Cart;

public interface CartDao extends JpaRepository<Cart, String> {

    Optional<Cart> findByUsersIdAndProductId(String username, String productId);

    List<Cart> findByUsersId(String username);
    
}
