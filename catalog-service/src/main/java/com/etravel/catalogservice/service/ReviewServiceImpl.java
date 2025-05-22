package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.contracts.IReviewRepository;
import com.etravel.catalogservice.domain.contracts.ITourPackageRepository;
import com.etravel.catalogservice.domain.dto.ReviewRequestDTO;
import com.etravel.catalogservice.domain.dto.ReviewResponseDTO;
import com.etravel.catalogservice.domain.entity.Review;
import com.etravel.catalogservice.domain.entity.TourPackage;
import com.etravel.catalogservice.domain.event.ReviewSubmittedEvent;
import com.etravel.catalogservice.domain.mapper.ReviewMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final IReviewRepository reviewRepo;
    private final ITourPackageRepository packageRepo;
    private final ReviewMapper mapper;

    public ReviewServiceImpl(IReviewRepository reviewRepo,
                             ITourPackageRepository packageRepo,
                             ReviewMapper mapper) {
        this.reviewRepo = reviewRepo;
        this.packageRepo = packageRepo;
        this.mapper = mapper;
    }

    @Override
    public List<ReviewResponseDTO> getByPackageId(Long packageId) {
        return reviewRepo.findByTourPackageId(packageId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewResponseDTO create(Long packageId, ReviewRequestDTO dto) {
        TourPackage pkg = packageRepo.findById(packageId)
                .orElseThrow(() -> new EntityNotFoundException("TourPackage not found: " + packageId));
        Review rev = mapper.toEntity(dto);
        pkg.addReview(rev);
        reviewRepo.save(rev);
        return mapper.toResponse(rev);
    }
}