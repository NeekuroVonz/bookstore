package com.theplayer.kits.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theplayer.kits.bookstore.entity.Users;

public interface UsersDao extends JpaRepository<Users, String> {
    
}
