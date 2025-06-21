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
import com.learn.entities.Books;
import com.learn.services.BooksService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BooksController {
    // Inject Service
    @Autowired
    private BooksService booksService;

    // Add Book
    @PostMapping
    public ResponseEntity<ResponseData<Books>> create(@Valid @RequestBody Books book, Errors errors) {
        ResponseData<Books> responseData = new ResponseData<>();
        // Jika terdapat error pada validasi
        if (errors.hasErrors()) {
            responseData.setStatus(false);
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.getMessages().add("No book added");
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        // Jika validasi ok
        responseData.setStatus(true);
        responseData.getMessages().add("Data berhasil ditambahkan");
        responseData.setPayload(booksService.save(book));
        return ResponseEntity.ok(responseData);
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
    public ResponseEntity<ResponseData<Books>> update(@Valid @RequestBody Books book, Errors errors) {
        ResponseData<Books> responseData = new ResponseData<>();
        // Jika terdapat error pada validasi
        if (errors.hasErrors()) {
            responseData.setStatus(false);
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.getMessages().add("No book edited");
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        // Jika validasi ok
        responseData.setStatus(true);
        responseData.getMessages().add("Data berhasil diubah");
        responseData.setPayload(booksService.save(book));
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public void removeOne(@PathVariable("id") Long id) {
        booksService.deleteOne(id);
    }
}
