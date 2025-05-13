package com.etravel.statisticsservice.service;

import com.etravel.statisticsservice.domain.dto.OverallStatisticsDTO;

import java.time.LocalDate;

public interface StatisticsService {
    /**
     * Collects all key metrics (reservations, revenue, ratings, trends) between fromDate and toDate.
     */
    OverallStatisticsDTO getOverallStatistics(LocalDate fromDate, LocalDate toDate);
}
