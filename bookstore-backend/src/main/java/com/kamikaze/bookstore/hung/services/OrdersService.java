package com.kamikaze.bookstore.hung.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kamikaze.bookstore.hung.dao.OrdersDao;
import com.kamikaze.bookstore.hung.dao.OrdersItemDao;
import com.kamikaze.bookstore.hung.dao.ProductDao;
import com.kamikaze.bookstore.hung.entity.Orders;
import com.kamikaze.bookstore.hung.entity.OrdersItem;
import com.kamikaze.bookstore.hung.entity.Product;
import com.kamikaze.bookstore.hung.entity.Users;
import com.kamikaze.bookstore.hung.exceptions.BadRequestException;
import com.kamikaze.bookstore.hung.exceptions.ResourceNotFoundException;
import com.kamikaze.bookstore.hung.model.CartRequest;
import com.kamikaze.bookstore.hung.model.OrdersRequest;
import com.kamikaze.bookstore.hung.model.OrdersResponse;
import com.kamikaze.bookstore.hung.model.OrdersStatus;

@Service
public class OrdersService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrdersItemDao ordersItemDao;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrdersLogService ordersLogService;

    // create order!
    @Transactional
    public OrdersResponse create(String username, OrdersRequest request) {
        Orders orders = new Orders();
        orders.setId(UUID.randomUUID().toString());
        orders.setDate(new Date());
        orders.setNumber(generateOrderNumber());
        orders.setUsers(new Users(username));
        orders.setShippingAddress(request.getShippingAddress());
        orders.setOrderStatus(OrdersStatus.DRAFT);
        orders.setOrderTime(new Date());

        List<OrdersItem> items = new ArrayList<>();
        for (CartRequest k : request.getItems()) {
            Product product = productDao.findById(k.getProductId())
                .orElseThrow(() -> new BadRequestException("Product ID: " + k.getProductId() + " not found!"));
            if (product.getStock() < k.getQuantity()) {
                throw new BadRequestException("Stock insufficient!");
            }
            OrdersItem pi = new OrdersItem();
            pi.setId((UUID.randomUUID().toString()));
            pi.setProduct(product);
            pi.setDescription(product.getName());
            pi.setQuantity(k.getQuantity());
            pi.setPrice(product.getPrice());
            pi.setAmount(new BigDecimal(pi.getPrice().doubleValue() * pi.getQuantity()));
            pi.setOrder(orders);
            items.add(pi);
        }

        BigDecimal amount = BigDecimal.ZERO;
        for (OrdersItem ordersItem : items) {
            amount = amount.add(ordersItem.getAmount());
        }

        orders.setAmount(amount);
        orders.setShipping(request.getShipping());
        orders.setTotal(orders.getAmount().add(orders.getShipping()));

        Orders saved = ordersDao.save(orders);
        for (OrdersItem ordersItem : items) {
            ordersItemDao.save(ordersItem);
            Product product = ordersItem.getProduct();
            product.setStock(product.getStock() - ordersItem.getQuantity());
            productDao.save(product);
            cartService.delete(username, product.getId());
        }

        // order log
        ordersLogService.createLog(username, orders, OrdersLogService.DRAFT, "Order Successfully!");
        OrdersResponse ordersResponse = new OrdersResponse(saved, items);
        return ordersResponse;
    }

    // cancel order
    @Transactional
    public Orders cancelOrder(String orderId, String userId) {
        Orders orders = ordersDao.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " not found!"));
        
        if (!userId.equals(orders.getUsers().getId())) {
            throw new BadRequestException("This order can only be canceled by the person concerned!");
        }

        if (!OrdersStatus.DRAFT.equals(orders.getOrderStatus())) {
            throw new BadRequestException("This order cannot be canceled because it has already been processed!");
        }

        orders.setOrderStatus(OrdersStatus.CANCELED);
        Orders saved = ordersDao.save(orders);
        ordersLogService.createLog(userId, saved, OrdersLogService.CANCELED, "Order Canceled Successful!");
        return saved;
    }

    // get all the order belong to the user!
    public List<Orders> findAllOrders(String userId, int page, int limit) {
        return ordersDao.findByUsersId(userId, PageRequest.of(page, limit, Sort.by("orderTime").descending()));
    }

    // searching order for admin
    public List<Orders> search(String filterText, int page, int limit) {
        return ordersDao.search(filterText.toLowerCase(), PageRequest.of(page, limit, Sort.by("orderTime").descending()));
    }

    // auto generate order number so dont mind bout it!
    private String generateOrderNumber() {
        return String.format("%016d", System.nanoTime());
    }

    // payment confirm by admin
    @Transactional
    public Orders paymentConfirmation(String orderId, String userId) {
        Orders orders = ordersDao.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " not found!"));

        if (!OrdersStatus.DRAFT.equals(orders.getOrderStatus())) {
            throw new BadRequestException("Order confirmation failed! The current order status is: " + orders.getOrderStatus().name());
        }

        orders.setOrderStatus(OrdersStatus.PAYMENT);
        Orders saved = ordersDao.save(orders);
        ordersLogService.createLog(userId, saved, OrdersLogService.PAYMENT, "Payment Success Confirmed!");
        return saved;
    }

    // change status into packing by admin
    @Transactional
    public Orders packing(String orderId, String userId) {
        Orders orders = ordersDao.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " not found!"));

        if (!OrdersStatus.PAYMENT.equals(orders.getOrderStatus())) {
            throw new BadRequestException("Order packing failed! The current order status is: " + orders.getOrderStatus().name());
        }

        orders.setOrderStatus(OrdersStatus.PACKING);
        Orders saved = ordersDao.save(orders);
        ordersLogService.createLog(userId, saved, OrdersLogService.PACKING, "Your Order Is Being Prepared!");
        return saved;
    }

    // change status into send by admin
    @Transactional
    public Orders send(String orderId, String userId) {
        Orders orders = ordersDao.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " not found!"));

        if (!OrdersStatus.PACKING.equals(orders.getOrderStatus())) {
            throw new BadRequestException("Order delivery failed! The current order status is: " + orders.getOrderStatus().name());
        }

        orders.setOrderStatus(OrdersStatus.DELIVERY);
        Orders saved = ordersDao.save(orders);
        ordersLogService.createLog(userId, saved, OrdersLogService.DELIVERY, "Your Order Is Being Sent!");
        return saved;
    }

     // accept order
     @Transactional
     public Orders acceptOrder(String orderId, String userId) {
         Orders orders = ordersDao.findById(orderId)
             .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " not found!"));
         
         if (!userId.equals(orders.getUsers().getId())) {
             throw new BadRequestException("This order can only be canceled by the person concerned!");
         }
 
         if (!OrdersStatus.DELIVERY.equals(orders.getOrderStatus())) {
             throw new BadRequestException("Receipt failed! The current order status is: " + orders.getOrderStatus().name());
         }
 
         orders.setOrderStatus(OrdersStatus.FINISHED);
         Orders saved = ordersDao.save(orders);
         ordersLogService.createLog(userId, saved, OrdersLogService.FINISHED, "Order Canceled Successful!");
         return saved;
     }

}
