package com.kamikaze.bookstore.hung.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Users implements Serializable {

    @Id
    private String id;
    @JsonIgnore
    private String password;
    private String name;
    @JsonIgnore
    private String address;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String phone;
    @JsonIgnore
    private String roles;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;
    @JsonIgnore
    private Boolean isActive;

    public Users(String username) {
        this.id = username;
    }

}
