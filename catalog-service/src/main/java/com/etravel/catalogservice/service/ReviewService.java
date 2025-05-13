package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.dto.ReviewRequestDTO;
import com.etravel.catalogservice.domain.dto.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewResponseDTO> getByPackageId(Long packageId);
    ReviewResponseDTO create(Long packageId, ReviewRequestDTO dto);
}