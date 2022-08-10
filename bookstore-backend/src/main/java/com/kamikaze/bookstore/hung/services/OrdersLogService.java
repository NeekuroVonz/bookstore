package com.kamikaze.bookstore.hung.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamikaze.bookstore.hung.dao.OrdersLogDao;
import com.kamikaze.bookstore.hung.entity.Orders;
import com.kamikaze.bookstore.hung.entity.OrdersLog;
import com.kamikaze.bookstore.hung.entity.Users;

@Service
public class OrdersLogService {
    @Autowired
    private OrdersLogDao ordersLogDao;


    public final static int DRAFT = 0;
    public final static int PAYMENT = 10;
    public final static int PACKING = 20;
    public final static int DELIVERY = 30;
    public final static int FINISHED = 40;
    public final static int CANCELED = 90;

    public void createLog(String username, Orders orders, int type, String message) {
        OrdersLog p = new OrdersLog();
        p.setId(UUID.randomUUID().toString());
        p.setLogMessage(message);
        p.setLogType(type);
        p.setOrder(orders);
        p.setUsers(new Users(username));
        p.setTime(new Date());
        ordersLogDao.save(p);
    }
}
