package com.example.releasetracker.service;

import com.example.releasetracker.domain.entity.Release;
import com.example.releasetracker.domain.enums.ReleaseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReleaseWorkflowServiceTest {

    private ReleaseWorkflowService workflowService;

    @BeforeEach
    void setUp() {
        workflowService = new ReleaseWorkflowService();
    }

    @Test
    void shouldApplyValidTransition() {
        Release release = Release.builder()
                .status(ReleaseStatus.CREATED)
                .lastUpdateAt(LocalDateTime.now().minusDays(1))
                .build();

        workflowService.applyTransition(release, ReleaseStatus.IN_DEVELOPMENT);

        assertEquals(ReleaseStatus.IN_DEVELOPMENT, release.getStatus());
        assertNotNull(release.getLastUpdateAt());
    }

    @Test
    void shouldThrowExceptionForInvalidTransition() {
        Release release = Release.builder()
                .status(ReleaseStatus.CREATED)
                .build();

        assertThrows(IllegalArgumentException.class, () ->
                workflowService.applyTransition(release, ReleaseStatus.ON_PROD)
        );
    }

    @Test
    void validateTransition_shouldFailCorrectly() {
        Release release = Release.builder()
                .status(ReleaseStatus.DONE)
                .build();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> workflowService.validateTransition(release, ReleaseStatus.IN_DEVELOPMENT)
        );

        assertTrue(ex.getMessage().contains("Cannot change status"));
    }
}
