package com.antipin.library.controller;

import com.antipin.library.model.Author;
import com.antipin.library.model.Book;
import com.antipin.library.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@AutoConfigureMockMvc
@Transactional
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final static String URL = "/api/v1/books";

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private BookRepository repository;

    @Test
    void whenCreateNewBookThenReturn201() throws Exception {
        Author newAuthor = new Author(null, "NewFirstname", "NewSurname", new ArrayList<>());
        Book newBook = new Book(null, "New Title", Set.of(newAuthor), 2024);
        mockMvc.perform(post(URL).content(mapper.writeValueAsString(newBook))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title").value(newBook.getTitle()))
                .andExpect(jsonPath("$.publicationYear").value(newBook.getPublicationYear()));
    }

    @Test
    void whenGetSingleBookThenReturn200() throws Exception {
        mockMvc.perform(get(URL + "/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Book Title 1"))
                .andExpect(jsonPath("$.publicationYear").value(2020));
    }

    @Test
    void whenPutThenUpdateCorrectly() throws Exception {
        Book bookToUpdate = repository.findById(1L).orElseThrow();
        bookToUpdate.setTitle("Updated Title");
        mockMvc.perform(put(URL).contentType("application/json")
                        .content(mapper.writeValueAsString(bookToUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void whenDeleteThenReturn204() throws Exception {
        mockMvc.perform(delete(URL + "/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThat(repository.findById(1L)).isEmpty();
    }

    @Test
    void whenGetAllBooksThenReturnPages() throws Exception {
        mockMvc.perform(get(URL)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(3)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(3));
    }
}
