package com.learn.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.dto.ResponseData;
import com.learn.entities.Author;
import com.learn.entities.Books;
import com.learn.services.AuthorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<ResponseData<Author>> create(@Valid @RequestBody Author author, Errors errors) {
        ResponseData<Author> responseData = new ResponseData<>();
        // Jika terdapat error pada validasi
        if (errors.hasErrors()) {
            responseData.setStatus(false);
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.getMessages().add("No Author added");
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        // Jika validasi ok
        responseData.setStatus(true);
        responseData.getMessages().add("Data berhasil ditambahkan");
        responseData.setPayload(authorService.save(author));
        return ResponseEntity.ok(responseData);
    }

    @PutMapping
    public ResponseEntity<ResponseData<Author>> update(@RequestBody Author author, Errors errors) {
        ResponseData<Author> responseData = new ResponseData<>();
        // Jika terdapat error pada validasi
        if (errors.hasErrors()) {
            responseData.setStatus(false);
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.getMessages().add("No Author added");
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        // Jika validasi ok
        responseData.setStatus(true);
        responseData.getMessages().add("Data berhasil diubah");
        responseData.setPayload(authorService.save(author));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping
    public Iterable<Author> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public Author findOne(@PathVariable("id") Long id) {
        return authorService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable("id") Long id) {
        authorService.deleteOne(id);
    }
}
