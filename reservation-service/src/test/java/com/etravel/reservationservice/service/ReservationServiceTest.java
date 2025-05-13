package com.etravel.reservationservice.service;

import com.etravel.reservationservice.domain.contracts.ReservationRepository;
import com.etravel.reservationservice.domain.dto.ReservationRequestDTO;
import com.etravel.reservationservice.domain.dto.ReservationResponseDTO;
import com.etravel.reservationservice.domain.entity.Reservation;
import com.etravel.reservationservice.domain.entity.ReservationStatus;
import com.etravel.reservationservice.domain.mapper.ReservationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository repo;
    @Mock
    private ReservationMapper mapper;
    @InjectMocks
    private ReservationServiceImpl service;

    private ReservationRequestDTO req;
    private Reservation reservation;
    private ReservationResponseDTO resp;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        req = new ReservationRequestDTO();
        req.setClientId("1");
        req.setTourPackageId("2");

        reservation = new Reservation(1L, 2L, LocalDateTime.now(), ReservationStatus.PENDING);
        resp = new ReservationResponseDTO();
        resp.setId("1");
        resp.setClientId("1");
        resp.setTourPackageId("2");
        resp.setStatus("PENDING");
    }

    @Test
    void testCreate() {
        when(mapper.toEntity(req)).thenReturn(reservation);
        when(repo.save(reservation)).thenReturn(reservation);
        when(mapper.toResponse(reservation)).thenReturn(resp);

        ReservationResponseDTO result = service.createReservation(req);
        assertEquals("1", result.getId());
        verify(repo).save(reservation);
    }

    @Test
    void testGetByIdFound() {
        when(repo.findById(1L)).thenReturn(Optional.of(reservation));
        when(mapper.toResponse(reservation)).thenReturn(resp);

        ReservationResponseDTO result = service.getReservationById(1L);
        assertEquals("1", result.getId());
    }

    @Test
    void testGetByIdNotFound() {
        when(repo.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getReservationById(3L));
    }

    @Test
    void testListByClient() {
        when(repo.findByClientId(1L)).thenReturn(Collections.singletonList(reservation));
        when(mapper.toResponse(reservation)).thenReturn(resp);

        var list = service.getReservationsByClientId(1L);
        assertEquals(1, list.size());
    }

    @Test
    void testDelete() {
        doNothing().when(repo).deleteById(1L);
        service.deleteReservation(1L);
        verify(repo).deleteById(1L);
    }
}