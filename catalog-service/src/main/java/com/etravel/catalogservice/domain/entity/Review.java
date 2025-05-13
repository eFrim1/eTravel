package com.etravel.catalogservice.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long clientId;

    @NotNull
    private Integer rating;       // e.g. 1–5

    @NotNull
    private String comment;

    @NotNull
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_package_id", nullable = false)
    private TourPackage tourPackage;

    protected Review() { }

    public Review(
            @NotNull Long clientId,
            @NotNull Integer rating,
            @NotNull String comment,
            @NotNull LocalDateTime createdAt
    ) {
        this.clientId  = clientId;
        this.rating    = rating;
        this.comment   = comment;
        this.createdAt = createdAt;
    }

    // getters & setters omitted for brevity

    void setTourPackage(TourPackage tp) {
        this.tourPackage = tp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Long getClientId() {
        return clientId;
    }

    public void setClientId(@NotNull Long clientId) {
        this.clientId = clientId;
    }

    public @NotNull Integer getRating() {
        return rating;
    }

    public void setRating(@NotNull Integer rating) {
        this.rating = rating;
    }

    public @NotNull String getComment() {
        return comment;
    }

    public void setComment(@NotNull String comment) {
        this.comment = comment;
    }

    public @NotNull LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NotNull LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TourPackage getTourPackage() {
        return tourPackage;
    }
}