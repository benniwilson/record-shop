package com.northcoders.record_shop.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDate;

@Entity
@Builder
@Getter
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
    public long id;

    @Column
    public String name;

    @Column
    public String artist;

    @Column
    public Genre genre;

    @Column
    public LocalDate dateReleased;

    @Column
    public double price;

    @Column
    public int stock;
}
