package com.etravel.reservationservice.service;

import com.etravel.reservationservice.domain.dto.ReservationRequestDTO;
import com.etravel.reservationservice.domain.dto.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {
    List<ReservationResponseDTO> getAllReservations();
    ReservationResponseDTO getReservationById(Long id);
    List<ReservationResponseDTO> getReservationsByClientId(Long clientId);
    List<ReservationResponseDTO> getReservationsByPackageId(Long packageId);
    ReservationResponseDTO createReservation(ReservationRequestDTO dto);
    void deleteReservation(Long id);
}
