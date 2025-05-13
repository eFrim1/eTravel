package com.etravel.catalogservice.infrastructure;

import com.etravel.catalogservice.domain.contracts.IReviewRepository;
import com.etravel.catalogservice.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IReviewRepositoryImpl extends IReviewRepository, JpaRepository<Review, Long> {
}
