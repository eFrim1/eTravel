package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.contracts.IReviewRepository;
import com.etravel.catalogservice.domain.contracts.ITourPackageRepository;
import com.etravel.catalogservice.domain.dto.ReviewRequestDTO;
import com.etravel.catalogservice.domain.dto.ReviewResponseDTO;
import com.etravel.catalogservice.domain.entity.Review;
import com.etravel.catalogservice.domain.entity.TourPackage;
import com.etravel.catalogservice.domain.mapper.ReviewMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private IReviewRepository reviewRepo;

    @Mock
    private ITourPackageRepository packageRepo;

    @Mock
    private ReviewMapper mapper;

    @InjectMocks
    private ReviewServiceImpl service;

    private ReviewRequestDTO request;
    private Review review;
    private ReviewResponseDTO responseDTO;
    private TourPackage pkg;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        request = new ReviewRequestDTO("10","4","Nice");
        review = new Review(10L,4,"Nice", LocalDateTime.now());
        responseDTO = new ReviewResponseDTO();
        responseDTO.setComment("Nice");

        pkg = new TourPackage("D", new java.math.BigDecimal("1.0"),
                java.time.LocalDate.now(), java.time.LocalDate.now(),
                null,null,null);
    }

    @Test
    void testCreateSuccess() {
        when(packageRepo.findById(5L)).thenReturn(Optional.of(pkg));
        when(mapper.toEntity(request)).thenReturn(review);
        when(reviewRepo.save(review)).thenReturn(review);
        when(mapper.toResponse(review)).thenReturn(responseDTO);

        ReviewResponseDTO result = service.create(5L, request);
        assertEquals("Nice", result.getComment());
        verify(reviewRepo).save(review);
    }

    @Test
    void testCreatePackageNotFound() {
        when(packageRepo.findById(6L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.create(6L, request));
    }

    @Test
    void testGetByPackageId() {
        when(reviewRepo.findByTourPackageId(5L)).thenReturn(Collections.singletonList(review));
        when(mapper.toResponse(review)).thenReturn(responseDTO);

        List<ReviewResponseDTO> list = service.getByPackageId(5L);
        assertEquals(1, list.size());
        assertEquals("Nice", list.get(0).getComment());
    }
}