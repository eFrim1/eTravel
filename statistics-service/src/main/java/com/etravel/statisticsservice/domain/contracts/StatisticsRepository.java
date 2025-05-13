package com.etravel.statisticsservice.domain.contracts;

import java.time.LocalDate;
import java.util.Map;

public interface StatisticsRepository {
    /**
     * Count reservations grouped by package ID in the given period.
     * @return Map of packageId -> reservation count
     */
    Map<String, Long> countReservationsByPackage(LocalDate from, LocalDate to);

    /**
     * Sum of package revenues (price x bookings) grouped by package ID in the given period.
     * @return Map of packageId -> total revenue
     */
    Map<String, Double> sumRevenueByPackage(LocalDate from, LocalDate to);

    /**
     * Average review rating grouped by package ID in the given period.
     * @return Map of packageId -> average rating
     */
    Map<String, Double> averageRatingByPackage(LocalDate from, LocalDate to);

    /**
     * Count reservations per day in the given date range.
     * @return Map of date -> reservation count
     */
    Map<java.time.LocalDate, Long> countReservationsOverTime(LocalDate from, LocalDate to);
}