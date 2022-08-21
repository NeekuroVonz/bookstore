package com.kamikaze.bookstore.hung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamikaze.bookstore.hung.entity.Users;
import com.kamikaze.bookstore.hung.services.UsersService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// http://localhost:9999/bookstore/users

@RestController
@RequestMapping("/bookstore")
@PreAuthorize("isAuthenticated()")
public class UsersController {
    
    @Autowired
    private UsersService usersService;

    @GetMapping(value="/users")
    public List<Users> findAll() {
        return usersService.findAll();
    }
    
    @GetMapping(value="/users/{id}")
    public Users findById(@PathVariable("id") String id) {
        return usersService.findById(id);
    }

    // @PostMapping(value="/users")
    // public Users create(@RequestBody Users users, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
    //     return usersService.create(users, getSiteURL(request));
    // }
    // private String getSiteURL(HttpServletRequest request) {
    //     String siteURL = request.getRequestURL().toString();
    //     return siteURL.replace(request.getServletPath(), "");
    // }
    // @GetMapping("/verify")
    // public String verifyUser(@Param("code") String code) {
    //     if (usersService.verify(code)) {
    //         return "verify_success";
    //     } else {
    //         return "verify_fail";
    //     }
    // }

    @PutMapping(value="/users")
    public Users edit(@RequestBody Users users) {
        return usersService.edit(users);
    }
    
    @DeleteMapping(value = "/users/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public void deleteById(@PathVariable("id") String id) {
        usersService.deleteById(id);
    }

}
