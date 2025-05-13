package com.example.bookcatalog.service;

import com.example.bookcatalog.model.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // given
        List<Book> books = Arrays.asList(
                new Book("123", "Title1", "Author1"),
                new Book("456", "Title2", "Author2")
        );
        when(bookRepository.findAll()).thenReturn(books);

        // when
        List<Book> result = bookService.getAllBooks();

        // then
        assertEquals(2, result.size());
        assertEquals("123", result.get(0).getIsbn());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Author1", result.get(0).getAuthor());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookByIsbn() {
        // given
        Book book = new Book("123", "Title1", "Author1");
        when(bookRepository.findById("123")).thenReturn(Optional.of(book));

        // when
        Book result = bookService.getBookByIsbn("123");

        // then
        assertNotNull(result);
        assertEquals("123", result.getIsbn());
        assertEquals("Title1", result.getTitle());
        verify(bookRepository, times(1)).findById("123");
    }

    @Test
    void testCreateBook() {
        // given
        Book book = new Book("123", "Title1", "Author1");
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // when
        Book result = bookService.createBook(book);

        // then
        assertNotNull(result);
        assertEquals("123", result.getIsbn());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateBook() {
        // given
        String isbn = "123";
        Book book = new Book(isbn, "Updated Title", "Updated Author");
        when(bookRepository.existsById(isbn)).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // when
        Book result = bookService.updateBook(isbn, book);

        // then
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
        verify(bookRepository, times(1)).existsById(isbn);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testDeleteBook() {
        // given
        String isbn = "123";
        when(bookRepository.existsById(isbn)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(isbn);

        // when
        bookService.deleteBook(isbn);

        // then
        verify(bookRepository, times(1)).existsById(isbn);
        verify(bookRepository, times(1)).deleteById(isbn);
    }
}
