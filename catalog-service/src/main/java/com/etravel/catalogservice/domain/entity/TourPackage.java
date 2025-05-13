package com.etravel.catalogservice.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tour_packages")
public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String destination;

    @NotNull
    private BigDecimal price;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    // up to 3 image URLs
    private String image1;
    private String image2;
    private String image3;

    @OneToMany(mappedBy = "tourPackage",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    protected TourPackage() { }

    public TourPackage(
            @NotNull String destination,
            @NotNull BigDecimal price,
            @NotNull LocalDate startDate,
            @NotNull LocalDate endDate,
            String image1,
            String image2,
            String image3
    ) {
        this.destination = destination;
        this.price       = price;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.image1      = image1;
        this.image2      = image2;
        this.image3      = image3;
    }

    // getters & setters omitted for brevity

    public void addReview(Review review) {
        reviews.add(review);
        review.setTourPackage(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setTourPackage(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getDestination() {
        return destination;
    }

    public void setDestination(@NotNull String destination) {
        this.destination = destination;
    }

    public @NotNull BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotNull BigDecimal price) {
        this.price = price;
    }

    public @NotNull LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull LocalDate startDate) {
        this.startDate = startDate;
    }

    public @NotNull LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}