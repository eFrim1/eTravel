package com.etravel.reservationservice.domain.contracts;

import com.etravel.reservationservice.domain.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Optional<Reservation> findById(Long id);
    List<Reservation> findAll();
    List<Reservation> findByClientId(Long clientId);
    List<Reservation> findByTourPackageId(Long packageId);
    List<Reservation> findByReservedAtBetween(LocalDateTime from, LocalDateTime to);
    Reservation save(Reservation reservation);
    void deleteById(Long id);
}