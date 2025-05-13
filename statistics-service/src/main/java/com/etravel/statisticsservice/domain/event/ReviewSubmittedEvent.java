package com.etravel.statisticsservice.domain.event;

import java.time.LocalDateTime;

public class ReviewSubmittedEvent {
    private Long id;
    private Long tourPackageId;
    private int rating;
    private LocalDateTime createdAt;

    public ReviewSubmittedEvent() {}
    public ReviewSubmittedEvent(Long id, Long tourPackageId, int rating, LocalDateTime createdAt) {
        this.id = id;
        this.tourPackageId = tourPackageId;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTourPackageId() {
        return tourPackageId;
    }

    public void setTourPackageId(Long tourPackageId) {
        this.tourPackageId = tourPackageId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}