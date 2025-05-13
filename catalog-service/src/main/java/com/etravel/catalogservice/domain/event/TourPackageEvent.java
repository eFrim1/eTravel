package com.etravel.catalogservice.domain.event;

import java.math.BigDecimal;

public class TourPackageEvent {
    private Long id;
    private String destination;
    private BigDecimal price;
    public TourPackageEvent(Long id,String dest,BigDecimal price){
        this.id=id;this.destination=dest;this.price=price;
    }

    public Long getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public BigDecimal getPrice() {
        return price;
    }
}