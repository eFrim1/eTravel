package com.etravel.catalogservice.domain.mapper;

import com.etravel.catalogservice.domain.dto.ReviewRequestDTO;
import com.etravel.catalogservice.domain.dto.ReviewResponseDTO;
import com.etravel.catalogservice.domain.entity.Review;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {


    public ReviewResponseDTO toResponse(Review entity) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(entity.getId().toString());
        dto.setClientId(entity.getClientId().toString());
        dto.setRating(entity.getRating().toString());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt().toString());
        return dto;
    }

    public Review toEntity(ReviewRequestDTO dto) {
        return new Review(
                Long.valueOf(dto.getClientId()),
                Integer.valueOf(dto.getRating()),
                dto.getComment(),
                LocalDateTime.now()
        );
    }
}