package com.example.demo.service;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.DuplicateBookException;
import com.example.demo.model.Book;
import com.example.demo.model.BookRequest;
import com.example.demo.model.BookResponse;
import com.example.demo.repo.BookRepo;
import com.example.demo.utils.BookMappingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.demo.utils.BookMappingHelper.convertToDto;
import static com.example.demo.utils.BookMappingHelper.convertToEntity;


@Service
public class BookService {


    @Autowired
    BookRepo bookRepository;


    public Page<BookResponse> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(BookMappingHelper::convertToDto);

    }

    public BookResponse getBookById(int id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        return convertToDto(book);
    }

    public void addBook(BookRequest bookDto) {
        Book book = convertToEntity(bookDto);
        if (bookRepository.existsByNameAndAuthor(book.getName(), book.getAuthor())) {
            throw new DuplicateBookException("Book with same name and author already exists.");
        }
        bookRepository.save(book);
    }


    public void updateBook(int id, BookRequest bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        book.setName(bookDto.getName());
        book.setAuthor(bookDto.getAuthor());
        book.setPublisher(bookDto.getPublisher());
        book.setYearReleased(bookDto.getYearReleased());
        bookRepository.save(book);
    }
    public void deleteBook(int id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }
        bookRepository.deleteById(id);
    }

}
