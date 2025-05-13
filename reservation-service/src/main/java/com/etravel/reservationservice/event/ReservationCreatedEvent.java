package com.etravel.reservationservice.event;

import java.time.LocalDateTime;

public class ReservationCreatedEvent {
    private Long id;
    private Long tourPackageId;
    private LocalDateTime reservedAt;
    public ReservationCreatedEvent(Long id,Long pkg, LocalDateTime at){
        this.id=id;
        this.tourPackageId=pkg;
        this.reservedAt=at;
    }

    public Long getId() {
        return id;
    }

    public Long getTourPackageId() {
        return tourPackageId;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }
}