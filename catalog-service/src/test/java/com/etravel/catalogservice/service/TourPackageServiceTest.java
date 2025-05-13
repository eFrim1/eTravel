package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.contracts.ITourPackageRepository;
import com.etravel.catalogservice.domain.dto.TourPackageRequestDTO;
import com.etravel.catalogservice.domain.dto.TourPackageResponseDTO;
import com.etravel.catalogservice.domain.entity.TourPackage;
import com.etravel.catalogservice.domain.mapper.TourPackageMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourPackageServiceTest {

    @Mock
    private ITourPackageRepository repo;

    @Mock
    private TourPackageMapper mapper;

    @InjectMocks
    private TourPackageServiceImpl service;

    private TourPackageRequestDTO request;
    private TourPackage entity;
    private TourPackageResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        request = new TourPackageRequestDTO(
                "TestDest",
                "123.45",
                LocalDate.now().toString(),
                LocalDate.now().plusDays(3).toString(),
                "img1","img2","img3"
        );

        entity = new TourPackage(
                "TestDest",
                new BigDecimal("123.45"),
                LocalDate.parse(request.getStartDate()),
                LocalDate.parse(request.getEndDate()),
                "img1","img2","img3"
        );
        // simulate generated ID
        // using reflection or setter, assume setter exists
        // entity.setId(1L);

        responseDTO = new TourPackageResponseDTO();
        responseDTO.setId("1");
        responseDTO.setDestination("TestDest");
    }

    @Test
    void testCreate() {
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(responseDTO);

        TourPackageResponseDTO result = service.create(request);
        assertEquals("1", result.getId());
        verify(mapper).toEntity(request);
        verify(repo).save(entity);
        verify(mapper).toResponse(entity);
    }

    @Test
    void testGetByIdFound() {
        when(repo.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(responseDTO);

        TourPackageResponseDTO result = service.getById(1L);
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void testGetByIdNotFound() {
        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getById(2L));
    }

    @Test
    void testUpdate() {
        when(repo.findById(1L)).thenReturn(Optional.of(entity));
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(responseDTO);

        TourPackageResponseDTO updated = service.update(1L, request);
        assertEquals("1", updated.getId());
        verify(repo).save(entity);
    }

    @Test
    void testDelete() {
        doNothing().when(repo).deleteById(1L);
        service.delete(1L);
        verify(repo).deleteById(1L);
    }
}