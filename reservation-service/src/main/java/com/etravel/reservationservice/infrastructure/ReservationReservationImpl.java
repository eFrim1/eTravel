package com.etravel.reservationservice.infrastructure;

import com.etravel.reservationservice.domain.contracts.ReservationRepository;
import com.etravel.reservationservice.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationReservationImpl extends ReservationRepository, JpaRepository<Reservation, Long> {
}
