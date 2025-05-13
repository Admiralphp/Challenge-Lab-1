package com.example.bookcatalog.controller;

import com.example.bookcatalog.model.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        bookRepository.saveAll(Arrays.asList(
                new Book("111", "Test Book 1", "Test Author 1"),
                new Book("222", "Test Book 2", "Test Author 2")
        ));
    }

    @Test
    void testGetAllBooksEndpoint() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].isbn").value("111"))
                .andExpect(jsonPath("$[1].isbn").value("222"));
    }

    @Test
    void testGetBookByIsbnEndpoint() throws Exception {
        mockMvc.perform(get("/api/books/111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("111"))
                .andExpect(jsonPath("$.title").value("Test Book 1"))
                .andExpect(jsonPath("$.author").value("Test Author 1"));
    }

    @Test
    void testCreateBookEndpoint() throws Exception {
        String newBookJson = "{\"isbn\":\"333\",\"title\":\"New Book\",\"author\":\"New Author\"}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("333"))
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author").value("New Author"));
    }

    @Test
    void testUpdateBookEndpoint() throws Exception {
        String updatedBookJson = "{\"isbn\":\"111\",\"title\":\"Updated Book\",\"author\":\"Updated Author\"}";

        mockMvc.perform(put("/api/books/111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("111"))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author").value("Updated Author"));
    }

    @Test
    void testDeleteBookEndpoint() throws Exception {
        mockMvc.perform(delete("/api/books/111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify the book was deleted
        mockMvc.perform(get("/api/books/111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
