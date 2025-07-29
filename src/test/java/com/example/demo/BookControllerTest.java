package com.example.demo;

import com.example.demo.controller.BookController;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.BookRequest;
import com.example.demo.model.BookResponse;
import com.example.demo.service.BookService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // --- UNIT TESTS ---

    @Test
    public void testWelcome_shouldReturnWelcomeMessage() {
        Assert.assertEquals(bookController.welcome(), "Welcome to BookStore API Testing");
    }

    @DataProvider(name = "pageableProvider")
    public Object[][] pageableProvider() {
        return new Object[][] {
                { Pageable.unpaged() },
                { Pageable.ofSize(10) }
        };
    }

    @Test(dataProvider = "pageableProvider")
    public void testGetBooks_shouldReturnPaginatedResults(Pageable pageable) {
        BookResponse response = new BookResponse();
        response.setId(1);
        response.setName("Test Book");

        when(bookService.getBooks(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(response)));

        Page<BookResponse> result = bookController.getBooks(pageable);
        Assert.assertEquals(result.getContent().get(0).getName(), "Test Book");
    }

    @Test
    public void testGetBookById_shouldReturnBook() {
        BookResponse response = new BookResponse();
        response.setId(1);
        response.setName("Test Book");

        when(bookService.getBookById(1)).thenReturn(response);

        BookResponse result = bookController.getBookById(1);
        Assert.assertEquals(result.getId(), 1);
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void testGetBookById_whenNotFound_shouldThrowException() {
        when(bookService.getBookById(anyInt()))
                .thenThrow(new BookNotFoundException("Not Found"));

        bookController.getBookById(999);
    }

    @Test
    public void testAddBook_shouldInvokeService() {
        BookRequest request = new BookRequest();
        request.setName("Add Book");
        bookController.addBook(request);
        verify(bookService).addBook(any(BookRequest.class));
    }

    @Test
    public void testUpdateBook_shouldInvokeService() {
        BookRequest request = new BookRequest();
        request.setName("Updated Book");
        bookController.updateBook(1, request);
        verify(bookService).updateBook(eq(1), any(BookRequest.class));
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void testUpdateBook_whenNotFound_shouldThrowException() {
        doThrow(new BookNotFoundException("Not Found"))
                .when(bookService).updateBook(eq(1), any(BookRequest.class));
        bookController.updateBook(1, new BookRequest());
    }

    @Test
    public void testDeleteBook_shouldInvokeService() {
        bookController.deleteBook(1);
        verify(bookService).deleteBook(1);
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void testDeleteBook_whenNotFound_shouldThrowException() {
        doThrow(new BookNotFoundException("Not Found"))
                .when(bookService).deleteBook(1);
        bookController.deleteBook(1);
    }

    // --- COMPONENT TESTS (Direct method interaction with mocks) ---

    @Test
    public void testAddThenGetBook_shouldReturnAddedBook() {
        BookRequest request = new BookRequest();
        request.setName("CT Book");
        BookResponse response = new BookResponse();
        response.setId(1);
        response.setName("CT Book");

        when(bookService.getBooks(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(response)));

        bookController.addBook(request);
        Page<BookResponse> result = bookController.getBooks(Pageable.unpaged());

        Assert.assertTrue(result.getContent().stream()
                .anyMatch(book -> book.getName().equals("CT Book")));
    }

    @Test
    public void testAddThenUpdateBook_shouldReflectUpdate() {
        BookRequest request = new BookRequest();
        request.setName("Old Name");

        BookResponse updated = new BookResponse();
        updated.setId(1);
        updated.setName("New Name");

        when(bookService.getBookById(1)).thenReturn(updated);

        bookController.addBook(request);
        bookController.updateBook(1, request);

        BookResponse result = bookController.getBookById(1);
        Assert.assertEquals(result.getName(), "New Name");
    }

    @Test
    public void testAddThenDeleteBook_shouldThrowOnGet() {
        BookRequest request = new BookRequest();
        request.setName("Delete Me");

        doThrow(new BookNotFoundException("not found"))
                .when(bookService).getBookById(1);

        bookController.addBook(request);
        bookController.deleteBook(1);

        try {
            bookController.getBookById(1);
            Assert.fail("Expected BookNotFoundException");
        } catch (BookNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("not found"));
        }
    }


}
