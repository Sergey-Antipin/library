package com.antipin.library.controller;

import com.antipin.library.exception.EntityNotFoundException;
import com.antipin.library.model.Book;
import com.antipin.library.repository.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable("id") Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAll(Pageable pageable) {
        Page<Book> page = repository.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody @Valid Book book) {
        Book updatedBook = repository.save(book);
        URI newUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedBook.getId())
                .toUri();
        return ResponseEntity.created(newUri).body(updatedBook);
    }

    @PutMapping
    public ResponseEntity<Book> update(@RequestBody @Valid Book book) {
        return ResponseEntity.ok(repository.save(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> delete(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
