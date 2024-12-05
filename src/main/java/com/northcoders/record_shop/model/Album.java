package com.northcoders.record_shop.model;


import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@Entity
@Check(constraints = "stock > 0")
@Builder
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column
    String name;

    @Column
    String artist;

    @Column
    Genre genre;

    @Column(name = "date_released")
    LocalDate dateReleased;

    @Column
    double price;

    @Column(name = "stock")
    int stock;
}
