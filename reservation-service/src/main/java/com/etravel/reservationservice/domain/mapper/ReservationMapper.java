package com.etravel.reservationservice.domain.mapper;

import com.etravel.reservationservice.domain.dto.ReservationRequestDTO;
import com.etravel.reservationservice.domain.dto.ReservationResponseDTO;
import com.etravel.reservationservice.domain.entity.Reservation;
import com.etravel.reservationservice.domain.entity.ReservationStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationRequestDTO dto) {
        return new Reservation(
                Long.valueOf(dto.getClientId()),
                Long.valueOf(dto.getTourPackageId()),
                LocalDateTime.now(),
                ReservationStatus.PENDING
        );
    }

    public ReservationResponseDTO toResponse(Reservation entity) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(entity.getId().toString());
        dto.setClientId(entity.getClientId().toString());
        dto.setTourPackageId(entity.getTourPackageId().toString());
        dto.setReservedAt(entity.getReservedAt().toString());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }
}