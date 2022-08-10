package com.kamikaze.bookstore.hung.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kamikaze.bookstore.hung.entity.Orders;

public interface OrdersDao extends JpaRepository<Orders, String> {

    List<Orders> findByUsersId(String userId, Pageable pageable);

    @Query("SELECT p FROM Orders p WHERE LOWER(p.number) LIKE %:filterText% OR LOWER(p.users.name) LIKE %:filterText%")
    List<Orders> search(@Param("filterText") String filterText, Pageable pageable);
    
}
