package com.example.demo.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Book response object")
@Data
public class BookResponse {

    @Schema(description = "Unique book ID", example = "1")
    private int id;
    @Schema(description = "Book name", example = "1984")
    private String name;
    @Schema(description = "Author", example = "George Orwell")
    private String author;
    @Schema(description = "Publisher", example = "Secker & Warburg")
    private String publisher;
    @Schema(description = "Year Released", example = "1949")
    private int yearReleased;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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



    public BookResponse(int id, String name, String author, String publisher, int yearReleased) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.yearReleased = yearReleased;
    }


    public BookResponse() {
    }
}
