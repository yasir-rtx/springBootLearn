package com.learn.repositories;

import org.springframework.data.repository.CrudRepository;
import com.learn.entities.Author;
import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findByName(String name);
}
