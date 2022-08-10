package com.kamikaze.bookstore.hung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamikaze.bookstore.hung.entity.Cart;
import com.kamikaze.bookstore.hung.model.CartRequest;
import com.kamikaze.bookstore.hung.security.service.UsersDetailsImpl;
import com.kamikaze.bookstore.hung.services.CartService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

// http://localhost:9999/bookstore/cart

@RestController
@RequestMapping("/bookstore")
@PreAuthorize("isAuthenticated()")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping(value="/cart")
    public List<Cart> findByUserId(@AuthenticationPrincipal UsersDetailsImpl user) {
        return cartService.findByUsersId(user.getUsername());
    }

    @PostMapping(value="/cart")
    public Cart create(@AuthenticationPrincipal UsersDetailsImpl user, @RequestBody CartRequest request) {
        return cartService.addToCart(user.getUsername(), request.getProductId(), request.getQuantity());
    }

    @PatchMapping("/cart/{productId}")
    public Cart update(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("productId") String productId, @RequestParam("quantity") Double quantity) {
        return cartService.updateQuantity(user.getUsername(), productId, quantity);
    }  

    @DeleteMapping("cart/{productId}")
    public void delete(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("productId") String productId) {
        cartService.delete(user.getUsername(), productId);
    }

}
