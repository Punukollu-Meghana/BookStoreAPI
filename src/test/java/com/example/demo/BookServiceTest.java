package com.example.demo;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.DuplicateBookException;
import com.example.demo.model.Book;
import com.example.demo.model.BookRequest;
import com.example.demo.model.BookResponse;
import com.example.demo.repo.BookRepo;
import com.example.demo.service.BookService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepo bookRepo;

    private BookRequest request;
    private Book book;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new BookRequest("Title", "Author", "Publisher", 2000);
        book = new Book(1, "Title", "Author", "Publisher", 2000, 0);
    }

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // --- getBooks ---
    @Test
    public void testGetBooks() {
        Book book = new Book();
        book.setId(1);
        book.setName("Test Book");
        book.setAuthor("Author");

        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(book));
        when(bookRepo.findAll(any(Pageable.class))).thenReturn(bookPage);

        Page<BookResponse> result = bookService.getBooks(Pageable.unpaged());
        Assert.assertEquals(result.getContent().size(), 1);
        Assert.assertEquals(result.getContent().get(0).getName(), "Test Book");
    }

    // --- getBookById ---
    @Test
    public void testGetBookById() {
        Book book = new Book();
        book.setId(1);
        book.setName("Book 1");

        when(bookRepo.findById(1)).thenReturn(Optional.of(book));

        BookResponse result = bookService.getBookById(1);
        Assert.assertEquals(result.getName(), "Book 1");
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void testGetBookById_NotFoundUT() {
        when(bookRepo.findById(anyInt())).thenReturn(Optional.empty());
        bookService.getBookById(99);
    }

    // --- addBook ---
    @Test
    public void testAddBook_Success() {
        BookRequest req = new BookRequest();
        req.setName("New Book");
        req.setAuthor("Author");

        when(bookRepo.existsByNameAndAuthor(anyString(), anyString())).thenReturn(false);
        bookService.addBook(req);
        verify(bookRepo, times(1)).save(any(Book.class));
    }

    @Test(expectedExceptions = DuplicateBookException.class)
    public void testAddBook_DuplicateUT() {
        BookRequest req = new BookRequest();
        req.setName("New Book");
        req.setAuthor("Author");

        when(bookRepo.existsByNameAndAuthor(anyString(), anyString())).thenReturn(true);
        bookService.addBook(req);
    }

    // --- updateBook ---
    @Test
    public void testUpdateBook_Success() {
        BookRequest req = new BookRequest();
        req.setName("Updated");
        req.setAuthor("Updated Author");
        req.setPublisher("Pub");
        req.setYearReleased(2024);

        Book existing = new Book();
        existing.setId(1);

        when(bookRepo.findById(1)).thenReturn(Optional.of(existing));

        bookService.updateBook(1, req);

        verify(bookRepo, times(1)).save(existing);
        Assert.assertEquals(existing.getName(), "Updated");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testUpdateBook_NotFoundUT() {
        when(bookRepo.findById(999)).thenReturn(Optional.empty());
        bookService.updateBook(999, new BookRequest());
    }

    // --- deleteBook ---
    @Test
    public void testDeleteBook_Success() {
        when(bookRepo.existsById(1)).thenReturn(true);
        bookService.deleteBook(1);
        verify(bookRepo).deleteById(1);
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void testDeleteBook_NotFound() {
        when(bookRepo.existsById(1)).thenReturn(false);
        bookService.deleteBook(1);
    }
    @Test
    public void testGetBooks_WithResults() {
        Page<Book> page = new PageImpl<>(List.of(book));
        when(bookRepo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<BookResponse> result = bookService.getBooks(PageRequest.of(0, 10));
        Assert.assertEquals(result.getTotalElements(), 1);
        Assert.assertEquals(result.getContent().get(0).getName(), "Title");
    }

    @Test
    public void testGetBooks_EmptyResult() {
        when(bookRepo.findAll(any(PageRequest.class))).thenReturn(Page.empty());
        Page<BookResponse> result = bookService.getBooks(PageRequest.of(0, 10));
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetBookById_Found() {
        when(bookRepo.findById(1)).thenReturn(Optional.of(book));
        BookResponse response = bookService.getBookById(1);
        Assert.assertEquals(response.getName(), "Title");
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void testGetBookById_NotFound() {
        when(bookRepo.findById(anyInt())).thenReturn(Optional.empty());
        bookService.getBookById(100);
    }

    @Test
    public void testAddBook_NewEntry() {
        when(bookRepo.existsByNameAndAuthor(anyString(), anyString())).thenReturn(false);
        bookService.addBook(request);
        verify(bookRepo, times(1)).save(any(Book.class));
    }

    @Test(expectedExceptions = DuplicateBookException.class)
    public void testAddBook_Duplicate() {
        when(bookRepo.existsByNameAndAuthor(anyString(), anyString())).thenReturn(true);
        bookService.addBook(request);
    }

    @Test
    public void testUpdateBook_Existing() {
        when(bookRepo.findById(1)).thenReturn(Optional.of(book));
        BookRequest updated = new BookRequest("New", "New", "NewPub", 2022);

        bookService.updateBook(1, updated);

        verify(bookRepo, times(1)).save(book);
        Assert.assertEquals(book.getName(), "New");
        Assert.assertEquals(book.getYearReleased(), 2022);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testUpdateBook_NotFound() {
        when(bookRepo.findById(1)).thenReturn(Optional.empty());
        bookService.updateBook(1, request);
    }

    @Test
    public void testDeleteBook_ValidId() {
        when(bookRepo.existsById(1)).thenReturn(true);
        bookService.deleteBook(1);
        verify(bookRepo).deleteById(1);
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void testDeleteBook_InvalidId() {
        when(bookRepo.existsById(1)).thenReturn(false);
        bookService.deleteBook(1);
    }
}
