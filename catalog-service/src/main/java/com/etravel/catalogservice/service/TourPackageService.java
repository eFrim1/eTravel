package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.dto.TourPackageRequestDTO;
import com.etravel.catalogservice.domain.dto.TourPackageResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface TourPackageService {
    List<TourPackageResponseDTO> getAll();
    TourPackageResponseDTO getById(Long id);
    TourPackageResponseDTO create(TourPackageRequestDTO dto);
    TourPackageResponseDTO update(Long id, TourPackageRequestDTO dto);
    void delete(Long id);
    void export(String format, HttpServletResponse response);
    void exportByIds(List<Long> ids, String format, HttpServletResponse response);
}