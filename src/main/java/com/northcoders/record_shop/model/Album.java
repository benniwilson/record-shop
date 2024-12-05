package com.northcoders.record_shop.model;


import jakarta.persistence.*;
import lombok.Builder;


import java.time.LocalDate;

@Entity
@Builder
public class Album {

    public Album() {}

    public Album(long id, String name, String artist, Genre genre, LocalDate dateReleased, double price, int stock) {
        this.id = id;
        this.stock = stock;
        this.dateReleased = dateReleased;
        this.price = price;
        this.genre = genre;
        this.artist = artist;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    long id;

    @Column
    String name;

    @Column
    String artist;

    @Column
    Genre genre;

    @Column
    LocalDate dateReleased;

    @Column
    double price;

    @Column
    int stock;
}
