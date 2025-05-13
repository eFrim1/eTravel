package com.etravel.catalogservice.controller;

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
public class TourPackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TourPackageRequestDTO sample;

    @BeforeEach
    void init() {
        sample = new TourPackageRequestDTO(
                "Paris",
                new BigDecimal("1000.00").toString(),
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(5).toString(),
                "http://img1.jpg",
                null,
                null
        );
    }

    @Test
    void createReadUpdateDeleteFlow() throws Exception {
        // Create
        String createJson = objectMapper.writeValueAsString(sample);
        String location = mockMvc.perform(post("/api/tour-packages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").value("Paris"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract ID
        String id = objectMapper.readTree(location).get("id").asText();

        // Read
        mockMvc.perform(get("/api/tour-packages/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        // Update
        sample.setDestination("London");
        String updateJson = objectMapper.writeValueAsString(sample);
        mockMvc.perform(put("/api/tour-packages/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").value("London"));

        // Delete
        mockMvc.perform(delete("/api/tour-packages/{id}", id))
                .andExpect(status().isNoContent());

        // Confirm deletion
        mockMvc.perform(get("/api/tour-packages/{id}", id))
                .andExpect(status().isNotFound());
    }
}