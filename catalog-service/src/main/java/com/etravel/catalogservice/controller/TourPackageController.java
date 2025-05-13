package com.etravel.catalogservice.controller;

import com.etravel.catalogservice.domain.dto.TourPackageRequestDTO;
import com.etravel.catalogservice.domain.dto.TourPackageResponseDTO;
import com.etravel.catalogservice.service.TourPackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour-packages")
public class TourPackageController {
    private final TourPackageService service;

    public TourPackageController(TourPackageService service) {
        this.service = service;
    }

    @GetMapping
    public List<TourPackageResponseDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPackageResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<TourPackageResponseDTO> create(@RequestBody TourPackageRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourPackageResponseDTO> update(@PathVariable Long id,
                                                         @RequestBody TourPackageRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}