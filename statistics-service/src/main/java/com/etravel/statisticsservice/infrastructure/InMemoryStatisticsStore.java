package com.etravel.statisticsservice.infrastructure;

import com.etravel.statisticsservice.domain.contracts.StatisticsRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

@Component
public class InMemoryStatisticsStore implements StatisticsRepository {
    private final Map<String,LongAdder> reservationCounts = new ConcurrentHashMap<>();
    private final Map<String,DoubleAdder> revenueSums     = new ConcurrentHashMap<>();
    private final Map<String, DoubleAdder> ratingSums      = new ConcurrentHashMap<>();
    private final Map<String,LongAdder>   ratingCounts    = new ConcurrentHashMap<>();
    private final Map<LocalDate,LongAdder> dailyCounts    = new ConcurrentHashMap<>();

    /* mutation helpers */
    public void addReservation(String pkgId, double price, LocalDate date) {
        reservationCounts.computeIfAbsent(pkgId, k -> new LongAdder()).increment();
        revenueSums.computeIfAbsent(pkgId, k -> new DoubleAdder()).add(price);
        dailyCounts.computeIfAbsent(date, d -> new LongAdder()).increment();
    }
    public void addRating(String pkgId, int rating) {
        ratingSums.computeIfAbsent(pkgId, k -> new DoubleAdder()).add(rating);
        ratingCounts.computeIfAbsent(pkgId, k -> new LongAdder()).increment();
    }

    /* read‑model methods */
    @Override
    public Map<String, Long> countReservationsByPackage(LocalDate f, LocalDate t) {
        return reservationCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().sum()));
    }
    @Override
    public Map<String, Double> sumRevenueByPackage(LocalDate f, LocalDate t) {
        return revenueSums.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().sum()));
    }
    @Override
    public Map<String, Double> averageRatingByPackage(LocalDate f, LocalDate t) {
        return ratingSums.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> {
                            String k = e.getKey();
                            double sum = ratingSums.get(k).sum();
                            long   cnt = ratingCounts.getOrDefault(k, new LongAdder()).sum();
                            return cnt == 0 ? 0.0 : sum / cnt;
                        }));
    }
    @Override
    public Map<LocalDate, Long> countReservationsOverTime(LocalDate f, LocalDate t) {
        return dailyCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().sum()));
    }
}