package com.learn.repositories;

import java.util.Optional;

import com.learn.entities.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
