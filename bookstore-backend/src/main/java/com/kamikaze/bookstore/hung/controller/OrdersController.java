package com.kamikaze.bookstore.hung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamikaze.bookstore.hung.entity.Orders;
import com.kamikaze.bookstore.hung.model.OrdersRequest;
import com.kamikaze.bookstore.hung.model.OrdersResponse;
import com.kamikaze.bookstore.hung.security.service.UsersDetailsImpl;
import com.kamikaze.bookstore.hung.services.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// http://localhost:9999/bookstore/orders

@RestController
@RequestMapping("/bookstore")
@PreAuthorize("isAuthenticated()")
public class OrdersController {
    
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('user')")
    public OrdersResponse create(@AuthenticationPrincipal UsersDetailsImpl user, @RequestBody OrdersRequest request) {
        return ordersService.create(user.getUsername(), request);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasAuthority('user')")
    public Orders cancelOrderUser(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return ordersService.cancelOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/accept")
    @PreAuthorize("hasAuthority('user')")
    public Orders accept(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return ordersService.acceptOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/confirmation")
    @PreAuthorize("hasAuthority('admin')")
    public Orders confirmation(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return ordersService.paymentConfirmation(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/packing")
    @PreAuthorize("hasAuthority('admin')")
    public Orders packing(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return ordersService.packing(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/send")
    @PreAuthorize("hasAuthority('admin')")
    public Orders send(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return ordersService.send(orderId, user.getUsername());
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('user')")
    public List<Orders> findAllOrdersUsers(@AuthenticationPrincipal UsersDetailsImpl user,
                                            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                            @RequestParam(name = "limit", defaultValue = "25", required = false) int limit) {
        return ordersService.findAllOrders(user.getUsername(), page, limit);
    }

    @GetMapping("/orders/admin")
    @PreAuthorize("hasAuthority('admin')")
    public List<Orders> search(@AuthenticationPrincipal UsersDetailsImpl user,
                                @RequestParam(name = "filterText", defaultValue = "", required = false) String filterText,
                                @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                @RequestParam(name = "limit", defaultValue = "25", required = false) int limit) {
        return ordersService.search(filterText, page, limit);
    }
    

}
