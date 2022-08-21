package com.kamikaze.bookstore.hung.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kamikaze.bookstore.hung.entity.Users;

public interface UsersDao extends JpaRepository<Users, String> {

    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM Users u where u.verificationCode = ?1")
    public Users findByVerificationCode(String code);

    @Query("SELECT u FROM Users u WHERE u.email = ?1")
    public Users findByEmail(String email); 
     
    public Users findByResetPasswordToken(String token);
}
