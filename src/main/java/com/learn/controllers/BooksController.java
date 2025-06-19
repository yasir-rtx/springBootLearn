package com.learn.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.entities.Books;
import com.learn.services.BooksService;

@RestController
@RequestMapping("/api/books")
public class BooksController {
    // Inject Service
    @Autowired
    private BooksService booksService;

    @PostMapping
    public Books create(@RequestBody Books book) {
        return booksService.save(book);
    }

    @GetMapping
    public Iterable<Books> findAll() {
        return booksService.findAll();
    }

    @GetMapping("/{id}")
    public Books findOne(@PathVariable("id") Long id) {
        return booksService.findOne(id);
    }

    @PutMapping
    public Books update(@RequestBody Books book) {
        return booksService.save(book);
    }

    @DeleteMapping("/{id}")
    public void removeOne(@PathVariable("id") Long id) {
        booksService.deleteOne(id);
    }
}
