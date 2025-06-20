package com.learn.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.entities.Author;
import com.learn.repositories.AuthorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public Author findById(Long id) {
        // Cek jika id tidak ada
        Optional<Author> author = authorRepository.findById(id);
        if (!author.isPresent())
            return null;
        return author.get();
    }

    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    public void deleteOne(Long id) {
        authorRepository.deleteById(id);
    }

    public List<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }
}
