package com.etravel.statisticsservice.controller;

import com.etravel.statisticsservice.domain.dto.OverallStatisticsDTO;
import com.etravel.statisticsservice.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Get all statistics between two dates (inclusive).
     * @param fromDate start date (ISO format)
     * @param toDate end date (ISO format)
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<OverallStatisticsDTO> getOverallStatistics(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        OverallStatisticsDTO stats = statisticsService.getOverallStatistics(fromDate, toDate);
        return ResponseEntity.ok(stats);
    }
}