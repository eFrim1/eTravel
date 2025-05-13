package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.event.TourPackageEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KafkaPackageProducer {
    private final KafkaTemplate<String, TourPackageEvent> template;
    public KafkaPackageProducer(KafkaTemplate<String, TourPackageEvent> template){this.template=template;}

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(TourPackageEvent evt){
        template.send("packages", evt);
    }
}