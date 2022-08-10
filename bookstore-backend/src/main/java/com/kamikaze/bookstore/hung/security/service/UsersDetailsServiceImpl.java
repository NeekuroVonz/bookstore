package com.kamikaze.bookstore.hung.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kamikaze.bookstore.hung.dao.UsersDao;
import com.kamikaze.bookstore.hung.entity.Users;

@Service
public class UsersDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    UsersDao usersDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersDao.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));

        return UsersDetailsImpl.build(users);
    }

}
