package com.etravel.catalogservice.controller;

import com.etravel.catalogservice.domain.dto.ReviewRequestDTO;
import com.etravel.catalogservice.domain.dto.TourPackageRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String pkgId;

    @BeforeEach
    void setupPackage() throws Exception {
        TourPackageRequestDTO pkg = new TourPackageRequestDTO(
                "Rome",
                new BigDecimal("500.00").toString(),
                LocalDate.now().plusDays(2).toString(),
                LocalDate.now().plusDays(4).toString(),
                null,
                null,
                null
        );
        String pkgJson = objectMapper.writeValueAsString(pkg);
        String response = mockMvc.perform(post("/api/tour-packages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pkgJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        pkgId = objectMapper.readTree(response).get("id").asText();
    }

    @Test
    void createAndListReview() throws Exception {
        ReviewRequestDTO review = new ReviewRequestDTO(
                "12345",
                "5",
                "Excellent trip!"
        );

        // Add review
        String reviewJson = objectMapper.writeValueAsString(review);
        mockMvc.perform(post("/api/tour-packages/{pkgId}/reviews", pkgId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Excellent trip!"));

        // List reviews
        mockMvc.perform(get("/api/tour-packages/{pkgId}/reviews", pkgId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value("5"));
    }
}