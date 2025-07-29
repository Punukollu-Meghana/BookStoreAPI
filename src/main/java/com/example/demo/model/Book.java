package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Author")
    private String author;

    @JsonProperty("Publisher")
    private String publisher;

    @JsonProperty("Year Published")
    private int yearReleased;

    @Version
    private Integer version = 0;
}
