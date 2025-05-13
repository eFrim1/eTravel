package com.etravel.catalogservice.domain.contracts;

import com.etravel.catalogservice.domain.entity.TourPackage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITourPackageRepository {
    Optional<TourPackage> findById(Long id);
    List<TourPackage> findAll();
    List<TourPackage> findByDestination(String destination);
    List<TourPackage> findByStartDateBetween(LocalDate from, LocalDate to);
    List<TourPackage> findByPriceBetween(BigDecimal min, BigDecimal max);
    TourPackage save(TourPackage tourPackage);
    void deleteById(Long id);
}
