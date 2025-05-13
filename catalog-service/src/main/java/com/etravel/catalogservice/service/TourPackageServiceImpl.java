package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.contracts.ITourPackageRepository;
import com.etravel.catalogservice.domain.dto.TourPackageRequestDTO;
import com.etravel.catalogservice.domain.dto.TourPackageResponseDTO;
import com.etravel.catalogservice.domain.entity.TourPackage;
import com.etravel.catalogservice.domain.event.TourPackageEvent;
import com.etravel.catalogservice.domain.mapper.TourPackageMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourPackageServiceImpl implements TourPackageService {

    private final ApplicationEventPublisher publisher;
    private final ITourPackageRepository repo;
    private final TourPackageMapper mapper;

    public TourPackageServiceImpl(ITourPackageRepository repo, TourPackageMapper mapper, ApplicationEventPublisher publisher) {
        this.repo = repo;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    @Override
    public List<TourPackageResponseDTO> getAll() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TourPackageResponseDTO getById(Long id) {
        TourPackage pkg = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TourPackage not found: " + id));
        return mapper.toResponse(pkg);
    }

    @Override
    @Transactional
    public TourPackageResponseDTO create(TourPackageRequestDTO dto) {
        TourPackage pkg = mapper.toEntity(dto);
        publisher.publishEvent(new TourPackageEvent(pkg.getId(), pkg.getDestination(), pkg.getPrice()));
        return mapper.toResponse(repo.save(pkg));
    }

    @Override
    public TourPackageResponseDTO update(Long id, TourPackageRequestDTO dto) {
        TourPackage existing = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TourPackage not found: " + id));
        existing.setDestination(dto.getDestination());
        existing.setPrice(new BigDecimal(dto.getPrice()));
        existing.setStartDate(LocalDate.parse(dto.getStartDate()));
        existing.setEndDate(LocalDate.parse(dto.getEndDate()));
        existing.setImage1(dto.getImage1());
        existing.setImage2(dto.getImage2());
        existing.setImage3(dto.getImage3());
        return mapper.toResponse(repo.save(existing));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}