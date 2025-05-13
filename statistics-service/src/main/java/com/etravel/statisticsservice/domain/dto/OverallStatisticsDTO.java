package com.etravel.statisticsservice.domain.dto;

import java.time.LocalDate;
import java.util.Map;

public class OverallStatisticsDTO {
    private Map<String, Long> reservationsByPackage;
    private Map<String, Double> revenueByPackage;
    private Map<String, Double> averageRatingByPackage;
    private Map<LocalDate, Long> reservationsOverTime;

    public OverallStatisticsDTO() {}

    public Map<String, Long> getReservationsByPackage() {
        return reservationsByPackage;
    }
    public void setReservationsByPackage(Map<String, Long> reservationsByPackage) {
        this.reservationsByPackage = reservationsByPackage;
    }

    public Map<String, Double> getRevenueByPackage() {
        return revenueByPackage;
    }
    public void setRevenueByPackage(Map<String, Double> revenueByPackage) {
        this.revenueByPackage = revenueByPackage;
    }

    public Map<String, Double> getAverageRatingByPackage() {
        return averageRatingByPackage;
    }
    public void setAverageRatingByPackage(Map<String, Double> averageRatingByPackage) {
        this.averageRatingByPackage = averageRatingByPackage;
    }

    public Map<LocalDate, Long> getReservationsOverTime() {
        return reservationsOverTime;
    }
    public void setReservationsOverTime(Map<LocalDate, Long> reservationsOverTime) {
        this.reservationsOverTime = reservationsOverTime;
    }
}