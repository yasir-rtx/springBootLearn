package com.learn.models;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Table(name = "tb_users")
public class RegisterDto {
    @NotEmpty
    @Column(name = "user_username")
    private String username;

    @NotEmpty
    @Column(name = "user_name")
    private String name;

    @NotEmpty
    @Size(min = 6, message = "Minimum password length is 6 characters")
    @Column(name = "user_password")
    private String password;

    @NotEmpty
    @Column(name = "user_role")
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
