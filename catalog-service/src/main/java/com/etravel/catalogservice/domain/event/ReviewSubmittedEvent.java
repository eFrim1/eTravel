package com.etravel.catalogservice.domain.event;

import java.time.LocalDateTime;

public class ReviewSubmittedEvent {
    private Long id;
    private Long tourPackageId;
    private int rating;
    private LocalDateTime createdAt;
    public ReviewSubmittedEvent(Long id,Long pkg,int rating,LocalDateTime at){
        this.id=id;this.tourPackageId=pkg;this.rating=rating;this.createdAt=at;}

    public Long getId() {
        return id;
    }

    public Long getTourPackageId() {
        return tourPackageId;
    }

    public int getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}