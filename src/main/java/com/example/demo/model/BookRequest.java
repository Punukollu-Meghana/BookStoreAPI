package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema(description = "Book creation/update request")
@Data
public class BookRequest {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYearReleased() {
        return yearReleased;
    }

    public void setYearReleased(int yearReleased) {
        this.yearReleased = yearReleased;
    }

    public BookRequest(String name, String author, String publisher, int yearReleased) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.yearReleased = yearReleased;
    }

    @Schema(description = "Book name", example = "1984")
    private String name;
    @Schema(description = "Author of the book", example = "George Orwell")
    private String author;
    @Schema(description = "Publisher of the book", example = "Secker & Warburg")
    private String publisher;
    @Schema(description = "Year book was released", example = "1949")
    private int yearReleased;

    public BookRequest() {}
}
