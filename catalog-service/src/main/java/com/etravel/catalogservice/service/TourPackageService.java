package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.dto.TourPackageRequestDTO;
import com.etravel.catalogservice.domain.dto.TourPackageResponseDTO;

import java.util.List;

public interface TourPackageService {
    List<TourPackageResponseDTO> getAll();
    TourPackageResponseDTO getById(Long id);
    TourPackageResponseDTO create(TourPackageRequestDTO dto);
    TourPackageResponseDTO update(Long id, TourPackageRequestDTO dto);
    void delete(Long id);
}