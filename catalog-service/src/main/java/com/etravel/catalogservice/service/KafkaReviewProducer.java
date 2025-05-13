package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.event.ReviewSubmittedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KafkaReviewProducer {
    private final KafkaTemplate<String, ReviewSubmittedEvent> template;
    public KafkaReviewProducer(KafkaTemplate<String, ReviewSubmittedEvent> template){this.template=template;}

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ReviewSubmittedEvent evt){
        template.send("reviews", evt);
    }
}