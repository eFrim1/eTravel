package com.etravel.statisticsservice.domain.event;

import java.time.LocalDateTime;

public class ReservationCreatedEvent {
    private Long id;
    private Long tourPackageId;
    private double price;
    private LocalDateTime reservedAt;

    // default constructor for deserialization
    public ReservationCreatedEvent() {}
    public ReservationCreatedEvent(Long id, Long tourPackageId, double price, LocalDateTime reservedAt) {
        this.id = id;
        this.tourPackageId = tourPackageId;
        this.price = price;
        this.reservedAt = reservedAt;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }
}
