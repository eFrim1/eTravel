package com.etravel.defaultgateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class GatewayAdvancedTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void customHeaderIsForwarded() {
        // Without explicit timeout/bad-gateway filters, missing endpoints return 404
        webClient.get().uri("/catalog/api/tour-packages")
                .header("X-Test-Header","123")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("X-Test-Header","123");
    }

    @Test
    void missingDownstreamReturns404() {
        webClient.get().uri("/reservation/api/reservations/999/unknown")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void missingReviewRouteReturns404() {
        webClient.get().uri("/reviews/api/tour-packages/1/reviews/slow")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }
}