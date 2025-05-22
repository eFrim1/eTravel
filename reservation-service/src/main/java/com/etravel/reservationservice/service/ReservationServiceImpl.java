package com.etravel.reservationservice.service;

import com.etravel.reservationservice.domain.contracts.ReservationRepository;
import com.etravel.reservationservice.domain.dto.ReservationRequestDTO;
import com.etravel.reservationservice.domain.dto.ReservationResponseDTO;
import com.etravel.reservationservice.domain.entity.Reservation;
import com.etravel.reservationservice.domain.mapper.ReservationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repo;
    private final ReservationMapper mapper;

    public ReservationServiceImpl(ReservationRepository repo, ReservationMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public List<ReservationResponseDTO> getAllReservations() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponseDTO getReservationById(Long id) {
        Reservation res = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found: " + id));
        return mapper.toResponse(res);
    }

    @Override
    public List<ReservationResponseDTO> getReservationsByClientId(Long clientId) {
        return repo.findByClientId(clientId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponseDTO> getReservationsByPackageId(Long packageId) {
        return repo.findByTourPackageId(packageId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO dto) {
        Reservation entity = mapper.toEntity(dto);
        Reservation saved = repo.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public void deleteReservation(Long id) {
        repo.deleteById(id);
    }
}
