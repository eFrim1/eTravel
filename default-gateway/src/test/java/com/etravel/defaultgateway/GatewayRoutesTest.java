package com.etravel.defaultgateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GatewayRoutesTest {
    @Autowired
    private RouteLocator routeLocator;

    @Test
    void verifyRoutesExist() {
        boolean catalogExists = routeLocator.getRoutes()
                .filter(r -> "catalog".equals(r.getId()))
                .blockFirst() != null;
        assertTrue(catalogExists, "Catalog route should exist");

        boolean reservationExists = routeLocator.getRoutes()
                .filter(r -> "reservation".equals(r.getId()))
                .blockFirst() != null;
        assertTrue(reservationExists, "Reservation route should exist");

        boolean reviewExists = routeLocator.getRoutes()
                .filter(r -> "review".equals(r.getId()))
                .blockFirst() != null;
        assertTrue(reviewExists, "Review route should exist");

        boolean statisticsExists = routeLocator.getRoutes()
                .filter(r -> "statistics".equals(r.getId()))
                .blockFirst() != null;
        assertTrue(statisticsExists, "Statistics route should exist");
    }
}