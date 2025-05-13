package com.etravel.reservationservice.service;

import com.etravel.reservationservice.event.ReservationCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KafkaReservationProducer {
    private final KafkaTemplate<String, ReservationCreatedEvent> template;
    public KafkaReservationProducer(KafkaTemplate<String, ReservationCreatedEvent> template){this.template=template;}

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ReservationCreatedEvent evt){
        template.send("reservations", evt);
    }
}