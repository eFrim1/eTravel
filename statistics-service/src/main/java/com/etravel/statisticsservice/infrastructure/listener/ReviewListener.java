package com.etravel.statisticsservice.infrastructure.listener;

import com.etravel.statisticsservice.domain.event.ReviewSubmittedEvent;
import com.etravel.statisticsservice.infrastructure.InMemoryStatisticsStore;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewListener {
    private final InMemoryStatisticsStore store;

    public ReviewListener(InMemoryStatisticsStore store) {
        this.store = store;
    }

    @KafkaListener(topics = "reviews", groupId = "stats-service")
    public void handleReviewEvent(ReviewSubmittedEvent event) {
        store.addRating(
                event.getTourPackageId().toString(),
                event.getRating()
        );
    }
}