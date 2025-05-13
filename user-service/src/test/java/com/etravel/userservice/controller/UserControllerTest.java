package com.etravel.userservice.controller;

import com.etravel.userservice.domain.dto.UserRequestDTO;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDTO sample;

    @BeforeEach
    void init() {
        sample = new UserRequestDTO();
        sample.setUsername("jdoe");
        sample.setPassword("secret");
        sample.setEmail("jdoe@example.com");
        sample.setFirstName("John");
        sample.setLastName("Doe");
        sample.setRole("CLIENT");
    }

    @Test
    void createReadUpdateDeleteFlow() throws Exception {
        // Create
        String json = objectMapper.writeValueAsString(sample);
        String resp = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(resp).get("id").asText();

        // Read
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jdoe@example.com"));

        // Update
        sample.setFirstName("Jane");
        String updateJson = objectMapper.writeValueAsString(sample);
        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));

        // Delete
        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        // Confirm deletion
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }
}