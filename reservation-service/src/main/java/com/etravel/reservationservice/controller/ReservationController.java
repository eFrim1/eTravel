package com.etravel.reservationservice.controller;

import com.etravel.reservationservice.domain.dto.ReservationRequestDTO;
import com.etravel.reservationservice.domain.dto.ReservationResponseDTO;
import com.etravel.reservationservice.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_MANAGER')")
    public List<ReservationResponseDTO> getAll() {
        return service.getAllReservations();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ReservationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReservationById(id));
    }

    @GetMapping("/by-client/{clientId}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_MANAGER')")
    public List<ReservationResponseDTO> getByClient(@PathVariable Long clientId) {
        return service.getReservationsByClientId(clientId);
    }

    @GetMapping("/by-package/{pkgId}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_MANAGER')")
    public List<ReservationResponseDTO> getByPackage(@PathVariable("pkgId") Long pkgId) {
        return service.getReservationsByPackageId(pkgId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ReservationResponseDTO> create(@RequestBody ReservationRequestDTO dto) {
        return ResponseEntity.ok(service.createReservation(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}