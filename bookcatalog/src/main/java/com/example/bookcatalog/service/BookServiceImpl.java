package com.example.bookcatalog.service;

import com.example.bookcatalog.model.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;    @Override
    @Cacheable(value = "books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Cacheable(value = "book", key = "#isbn")
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with isbn: " + isbn));
    }

    @Override
    @CachePut(value = "book", key = "#book.isbn")
    @CacheEvict(value = "books", allEntries = true)
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @CachePut(value = "book", key = "#isbn")
    @CacheEvict(value = "books", allEntries = true)
    public Book updateBook(String isbn, Book book) {
        if (!bookRepository.existsById(isbn)) {
            throw new RuntimeException("Book not found with isbn: " + isbn);
        }
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "book", key = "#isbn"),
        @CacheEvict(value = "books", allEntries = true)
    })
    public void deleteBook(String isbn) {
        if (!bookRepository.existsById(isbn)) {
            throw new RuntimeException("Book not found with isbn: " + isbn);
        }
        bookRepository.deleteById(isbn);
    }
}
