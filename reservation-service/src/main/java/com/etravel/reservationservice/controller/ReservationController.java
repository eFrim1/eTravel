package com.etravel.reservationservice.controller;

import com.etravel.reservationservice.domain.dto.ReservationRequestDTO;
import com.etravel.reservationservice.domain.dto.ReservationResponseDTO;
import com.etravel.reservationservice.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationResponseDTO> getAll() {
        return service.getAllReservations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReservationById(id));
    }

    @GetMapping("/by-client/{clientId}")
    public List<ReservationResponseDTO> getByClient(@PathVariable Long clientId) {
        return service.getReservationsByClientId(clientId);
    }

    @GetMapping("/by-package/{pkgId}")
    public List<ReservationResponseDTO> getByPackage(@PathVariable("pkgId") Long pkgId) {
        return service.getReservationsByPackageId(pkgId);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> create(@RequestBody ReservationRequestDTO dto) {
        return ResponseEntity.ok(service.createReservation(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}