package com.kamikaze.bookstore.hung.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamikaze.bookstore.hung.entity.Users;
import com.kamikaze.bookstore.hung.model.JwtResponse;
import com.kamikaze.bookstore.hung.model.LoginRequest;
import com.kamikaze.bookstore.hung.model.SignUpRequest;
import com.kamikaze.bookstore.hung.security.jwt.JwtUtils;
import com.kamikaze.bookstore.hung.security.service.UsersDetailsImpl;
import com.kamikaze.bookstore.hung.services.UsersService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

//http://localhost:9999/auth/

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersService usersService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest request) {
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token =  jwtUtils.generateJwtToken(authentication);
        UsersDetailsImpl principal = (UsersDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok().body(new JwtResponse(token, principal.getUsername(), principal.getEmail()));
    }

    // sign up and verify email
    @PostMapping("/signup")
    public Users signup(@RequestBody SignUpRequest request, HttpServletRequest req) throws UnsupportedEncodingException, MessagingException {
        Users users = new Users();
        users.setId(request.getUsername());
        users.setEmail(request.getEmail());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setName(request.getName());
        users.setRoles("user");
        Users created = usersService.create(users, getSiteURL(req));
        return created;
    }
    public String getSiteURL(HttpServletRequest req) {
        String siteURL = req.getRequestURL().toString();
        return siteURL.replace(req.getServletPath(), "");
    }
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (usersService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

}
