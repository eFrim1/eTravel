package com.etravel.reservationservice.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long clientId;        // references User

    @NotNull
    private Long tourPackageId;   // references TourPackage

    @NotNull
    private LocalDateTime reservedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    protected Reservation() {}

    public Reservation(Long clientId,
                       Long tourPackageId,
                       LocalDateTime reservedAt,
                       ReservationStatus status) {
        this.clientId      = clientId;
        this.tourPackageId = tourPackageId;
        this.reservedAt    = reservedAt;
        this.status        = status;
    }

    public Long getId() { return id; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getTourPackageId() { return tourPackageId; }
    public void setTourPackageId(Long tourPackageId) { this.tourPackageId = tourPackageId; }

    public LocalDateTime getReservedAt() { return reservedAt; }
    public void setReservedAt(LocalDateTime reservedAt) { this.reservedAt = reservedAt; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}