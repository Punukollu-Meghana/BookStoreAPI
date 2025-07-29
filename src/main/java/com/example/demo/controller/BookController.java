package com.example.demo.controller;

import com.example.demo.model.BookRequest;
import com.example.demo.model.BookResponse;
import com.example.demo.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/books")
@Tag(name = "Book API", description = "Operations related to books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/welcome")
    @Operation(
            summary = "Welcome Message",
            description = "API welcome message",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "text/plain"))
    )
    public String welcome() {
        return "Welcome to BookStore API Testing";
    }

    @GetMapping
    @Operation(
            summary = "Get all books (paginated)",
            description = "Fetch paginated list of books",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Paginated list of books",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponse.class)))
    )
    public Page<BookResponse> getBooks(
            @Parameter(description = "Pageable object", hidden = true)
            Pageable pageable) {
        return bookService.getBooks(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get book by ID",
            description = "Retrieve a book by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book found", content = @Content(schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
            }
    )
    public BookResponse getBookById(
            @Parameter(description = "ID of the book to retrieve", example = "1")
            @PathVariable int id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @Operation(
            summary = "Add a new book",
            description = "Create a new book record",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Book request body",
                    content = @Content(schema = @Schema(implementation = BookRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book added successfully"),
                    @ApiResponse(responseCode = "409", description = "Duplicate book", content = @Content)
            }
    )
    public void addBook(
            @RequestBody BookRequest bookDto) {
        bookService.addBook(bookDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a book",
            description = "Update an existing book",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated book data",
                    content = @Content(schema = @Schema(implementation = BookRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
            }
    )
    public void updateBook(
            @Parameter(description = "ID of the book to update", example = "1")
            @PathVariable int id,
            @RequestBody BookRequest bookDto) {
        bookService.updateBook(id, bookDto);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a book",
            description = "Delete a book by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
            }
    )
    public void deleteBook(
            @Parameter(description = "ID of the book to delete", example = "1")
            @PathVariable int id) {
        bookService.deleteBook(id);
    }
}
