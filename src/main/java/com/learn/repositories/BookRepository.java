package com.learn.repositories;

import com.learn.entities.Books;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Books, Long> {
    // Fungsi pencarian berdasarkan nama
    // List<Books> findByNameContaining(String name);
}
