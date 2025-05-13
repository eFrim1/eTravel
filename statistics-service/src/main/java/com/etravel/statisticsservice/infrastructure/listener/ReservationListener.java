package com.etravel.statisticsservice.infrastructure.listener;

import com.etravel.statisticsservice.domain.event.ReservationCreatedEvent;
import com.etravel.statisticsservice.infrastructure.InMemoryStatisticsStore;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationListener {
    private final InMemoryStatisticsStore store;

    public ReservationListener(InMemoryStatisticsStore store) {
        this.store = store;
    }

    @KafkaListener(topics = "reservations", groupId = "stats-service")
    public void handleReservationEvent(ReservationCreatedEvent event) {
        store.addReservation(
                event.getTourPackageId().toString(),
                event.getPrice(),
                event.getReservedAt().toLocalDate()
        );
    }
}