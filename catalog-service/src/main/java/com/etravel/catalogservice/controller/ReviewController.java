package com.etravel.catalogservice.controller;

import com.etravel.catalogservice.domain.dto.ReviewRequestDTO;
import com.etravel.catalogservice.domain.dto.ReviewResponseDTO;
import com.etravel.catalogservice.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tour-packages/{pkgId}/reviews")
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
    public ResponseEntity<ReviewResponseDTO> create(@PathVariable("pkgId") Long pkgId,
                                                    @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(service.create(pkgId, dto));
    }
}