package com.etravel.catalogservice.domain.contracts;

import com.etravel.catalogservice.domain.entity.Review;

import java.util.List;
import java.util.Optional;

public interface IReviewRepository {
    Optional<Review> findById(Long id);
    List<Review> findByTourPackageId(Long tourPackageId);
    Review save(Review review);
    void deleteById(Long id);
}