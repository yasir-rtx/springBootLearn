package com.learn.services;

// import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.entities.Books;
import com.learn.repositories.BookRepository;

@Service
public class BooksService {

    // Inject Books Repository
    @Autowired
    private BookRepository bookRepo;

    // Create
    // Change from create to save
    // Save: Jika id sudah ada, maka akan update Else create
    public Books save(Books books) {
        return bookRepo.save(books);
    }

    // Fungsi Find one book
    public Books findOne(Long id) {
        // Cek jika id tidak ada
        Optional<Books> book = bookRepo.findById(id);
        if (!book.isPresent())
            return null;
        return book.get();
    }

    // Fungsi find all book
    public Iterable<Books> findAll() {
        return bookRepo.findAll();
    }

    // Delete one book
    public void deleteOne(Long id) {
        bookRepo.deleteById(id);
    }

    // Find book by name
    // public List<Books> findByName(String name) {
    // return bookRepo.findByNameContaining(name);
    // }
}
