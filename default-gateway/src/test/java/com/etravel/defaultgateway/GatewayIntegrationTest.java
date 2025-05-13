package com.etravel.defaultgateway;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;


import java.util.Map;

@SpringBootTest(
        classes = com.etravel.defaultgateway.GatewayIntegrationTest.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
class GatewayIntegrationTest {

    private static KafkaContainer kafka;
    private static GenericContainer<?> catalog;
    private static GenericContainer<?> reservation;
    private static GenericContainer<?> review;
    private static GenericContainer<?> user;
    private static GenericContainer<?> statistics;

    @BeforeAll
    static void setupContainers() {
        // Kafka
        DockerImageName kafkaImage = DockerImageName.parse("confluentinc/cp-kafka:latest")
                .asCompatibleSubstituteFor("apache/kafka");
        kafka = new KafkaContainer(kafkaImage);
        kafka.start();

        // Catalog+Review (same image)
        catalog = new GenericContainer<>("myrepo/catalog-service:latest")
                .withExposedPorts(4000)
                .withEnv("SPRING_KAFKA_BOOTSTRAP_SERVERS", kafka.getBootstrapServers());
        catalog.start();

        // Reservation
        reservation = new GenericContainer<>("myrepo/reservation-service:latest")
                .withExposedPorts(4002)
                .withEnv("SPRING_KAFKA_BOOTSTRAP_SERVERS", kafka.getBootstrapServers());
        reservation.start();

        // Review endpoint is part of catalog-service, no separate container

        // User
        user = new GenericContainer<>("myrepo/user-service:latest")
                .withExposedPorts(4001)
                .withEnv("SPRING_KAFKA_BOOTSTRAP_SERVERS", kafka.getBootstrapServers());
        user.start();

        // Statistics
        statistics = new GenericContainer<>("myrepo/statistics-service:latest")
                .withExposedPorts(4003)
                .withEnv("SPRING_KAFKA_BOOTSTRAP_SERVERS", kafka.getBootstrapServers());
        statistics.start();
    }

    @AfterAll
    static void teardown() {
        statistics.stop();
        user.stop();
        reservation.stop();
        catalog.stop();
        kafka.stop();
    }

    @Autowired
    private WebTestClient webClient;

    @Test
    void fullWorkflowThroughGateway() {
        // Create package
        webClient.post().uri("/catalog/api/tour-packages")
                .bodyValue(Map.of("destination","D","price","100"))
                .exchange().expectStatus().isOk();

        // Create user
        webClient.post().uri("/users/api/users")
                .bodyValue(Map.of("username","u","password","p","email","e@e","firstName","F","lastName","L","role","CLIENT"))
                .exchange().expectStatus().isOk();

        // Create reservation
        webClient.post().uri("/reservations/api/reservations")
                .bodyValue(Map.of("clientId","1","tourPackageId","1"))
                .exchange().expectStatus().isOk();

        // Fetch user list
        webClient.get().uri("/users/api/users")
                .exchange().expectStatus().isOk()
                .expectBody().jsonPath("$").isArray();

        // Submit review
        webClient.post().uri("/reviews/api/tour-packages/1/reviews")
                .bodyValue(Map.of("clientId","1","rating","5","comment","Good"))
                .exchange().expectStatus().isOk();

        // Fetch stats
        webClient.get().uri(uri -> uri
                        .path("/statistics/api/statistics")
                        .queryParam("from","2025-01-01")
                        .queryParam("to","2025-12-31").build())
                .exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.reservationsByPackage").isMap();
    }
}