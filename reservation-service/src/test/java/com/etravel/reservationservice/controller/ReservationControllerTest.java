package com.etravel.reservationservice.controller;

import com.etravel.reservationservice.domain.dto.ReservationRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private ReservationRequestDTO sample;

    @BeforeEach
    void init() {
        sample = new ReservationRequestDTO();
        sample.setClientId("1");
        sample.setTourPackageId("1");
    }

    @Test
    void createReadListDeleteFlow() throws Exception {
        String json = objectMapper.writeValueAsString(sample);
        String resp = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("1"))
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(resp).get("id").asText();

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(get("/api/reservations/by-client/{clientId}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientId").value("1"));

        mockMvc.perform(delete("/api/reservations/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isNotFound());
    }
}