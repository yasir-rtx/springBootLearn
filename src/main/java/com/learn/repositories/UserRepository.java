package com.learn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
}
