package com.etravel.catalogservice.domain.mapper;

import com.etravel.catalogservice.domain.dto.TourPackageRequestDTO;
import com.etravel.catalogservice.domain.dto.TourPackageResponseDTO;
import com.etravel.catalogservice.domain.entity.TourPackage;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TourPackageMapper {

    private ReviewMapper reviewMapper;

    public TourPackageMapper(ReviewMapper reviewMapper){
        this.reviewMapper = reviewMapper;
    }

    public TourPackageResponseDTO toResponse(TourPackage entity) {
        TourPackageResponseDTO dto = new TourPackageResponseDTO();
        dto.setId(entity.getId().toString());
        dto.setDestination(entity.getDestination());
        dto.setPrice(entity.getPrice().toString());
        dto.setStartDate(entity.getStartDate().toString());
        dto.setEndDate(entity.getEndDate().toString());

        dto.setImages(
                Arrays.stream(new String[]{
                                entity.getImage1(),
                                entity.getImage2(),
                                entity.getImage3()
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );

        dto.setReviews(
                entity.getReviews().stream()
                        .map(reviewMapper::toResponse)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    public TourPackage toEntity(TourPackageRequestDTO dto) {
        return new TourPackage(
                dto.getDestination(),
                new BigDecimal(dto.getPrice()),
                LocalDate.parse(dto.getStartDate()),
                LocalDate.parse(dto.getEndDate()),
                dto.getImage1(),
                dto.getImage2(),
                dto.getImage3()
        );
    }
}