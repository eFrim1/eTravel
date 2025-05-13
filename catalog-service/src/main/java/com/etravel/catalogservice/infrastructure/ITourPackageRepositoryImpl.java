package com.etravel.catalogservice.infrastructure;

import com.etravel.catalogservice.domain.contracts.ITourPackageRepository;
import com.etravel.catalogservice.domain.entity.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ITourPackageRepositoryImpl extends ITourPackageRepository, JpaRepository<TourPackage, Long> {
}
