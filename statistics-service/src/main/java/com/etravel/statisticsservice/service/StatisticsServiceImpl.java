package com.etravel.statisticsservice.service;

import com.etravel.statisticsservice.domain.contracts.StatisticsRepository;
import com.etravel.statisticsservice.domain.dto.OverallStatisticsDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository repository;

    public StatisticsServiceImpl(StatisticsRepository repository) {
        this.repository = repository;
    }

    @Override
    public OverallStatisticsDTO getOverallStatistics(LocalDate fromDate, LocalDate toDate) {
        OverallStatisticsDTO stats = new OverallStatisticsDTO();
        stats.setReservationsByPackage(
                repository.countReservationsByPackage(fromDate, toDate)
        );
        stats.setRevenueByPackage(
                repository.sumRevenueByPackage(fromDate, toDate)
        );
        stats.setAverageRatingByPackage(
                repository.averageRatingByPackage(fromDate, toDate)
        );
        stats.setReservationsOverTime(
                repository.countReservationsOverTime(fromDate, toDate)
        );
        return stats;
    }
}