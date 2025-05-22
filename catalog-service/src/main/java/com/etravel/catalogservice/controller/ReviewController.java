package com.etravel.catalogservice.controller;

import com.etravel.catalogservice.domain.dto.ReviewRequestDTO;
import com.etravel.catalogservice.domain.dto.ReviewResponseDTO;
import com.etravel.catalogservice.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/{pkgId}/reviews")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReviewResponseDTO> getByPackage(@PathVariable("pkgId") Long pkgId) {
        return service.getByPackageId(pkgId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<ReviewResponseDTO> create(@PathVariable("pkgId") Long pkgId,
                                                    @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(service.create(pkgId, dto));
    }
}