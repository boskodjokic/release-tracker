package com.example.releasetracker.controller;

import com.example.releasetracker.dto.ReleaseRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class ReleaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn201WhenCreated() throws Exception {
        ReleaseRequestDTO request = new ReleaseRequestDTO();
        request.setName("New Release");
        request.setReleaseDate(LocalDate.now());

        mockMvc.perform(post("/api/releases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("New Release"));
    }

    @Test
    void shouldReturnBadRequest_whenInvalidTransition() throws Exception {
        // Create release first
        ReleaseRequestDTO createRequest = new ReleaseRequestDTO();
        createRequest.setName("Release For Invalid Transition");
        createRequest.setReleaseDate(LocalDate.now());

        String createResponse = mockMvc.perform(post("/api/releases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long releaseId = objectMapper.readTree(createResponse).get("id").asLong();

        // Attempt illegal status transition (CREATED -> ON_PROD)
        ReleaseRequestDTO updateRequest = new ReleaseRequestDTO();
        updateRequest.setName("Release For Invalid Transition");
        updateRequest.setReleaseDate(LocalDate.now());
        updateRequest.setStatus("On PROD");  // Illegal transition

        mockMvc.perform(put("/api/releases/" + releaseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/releases/999999"))
                .andExpect(status().isNotFound());
    }
}

