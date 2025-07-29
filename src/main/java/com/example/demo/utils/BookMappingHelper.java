package com.example.demo.utils;

import com.example.demo.model.Book;
import com.example.demo.model.BookRequest;
import com.example.demo.model.BookResponse;

public class BookMappingHelper {

    public static BookResponse convertToDto(Book book) {
        BookResponse dto = new BookResponse();
        dto.setId(book.getId());
        dto.setName(book.getName());
        dto.setAuthor(book.getAuthor());
        dto.setPublisher(book.getPublisher());
        dto.setYearReleased(book.getYearReleased());
        return dto;
    }

    public static Book convertToEntity(BookRequest dto) {
        Book book = new Book();
        book.setName(dto.getName());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setYearReleased(dto.getYearReleased());
        return book;
    }
}
