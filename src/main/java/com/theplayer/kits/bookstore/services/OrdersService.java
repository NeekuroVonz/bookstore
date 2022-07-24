package com.theplayer.kits.bookstore.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.theplayer.kits.bookstore.dao.OrdersDao;
import com.theplayer.kits.bookstore.entity.Orders;
import com.theplayer.kits.bookstore.exceptions.ResourceNotFoundException;

@Service
public class OrdersService {

    @Autowired
    private OrdersDao ordersDao;

    public Orders findById(String id) {
        return ordersDao.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + id + " not found!"));
    }

    public List<Orders> findAll() {
        return ordersDao.findAll();
    }
    
    public Orders create(Orders orders) {
        orders.setId(UUID.randomUUID().toString());
        return ordersDao.save(orders);
    }

    public Orders edit(Orders orders) {
        return ordersDao.save(orders);
    }

    public void deleteById(String id) {
        ordersDao.deleteById(id);
    }
}
