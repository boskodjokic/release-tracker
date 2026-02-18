package com.example.releasetracker.service;

import com.example.releasetracker.domain.entity.Release;
import com.example.releasetracker.domain.enums.ReleaseStatus;
import com.example.releasetracker.dto.ReleaseRequestDTO;
import com.example.releasetracker.dto.ReleaseResponseDTO;
import com.example.releasetracker.exception.ReleaseNotFoundException;
import com.example.releasetracker.mapper.ReleaseMapper;
import com.example.releasetracker.repository.ReleaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
class ReleaseServiceTest {

    @Mock
    private ReleaseRepository releaseRepository;

    @Mock
    private ReleaseMapper releaseMapper;

    @Mock
    private ReleaseWorkflowService workflowService;

    @InjectMocks
    private ReleaseService releaseService;

    private Release release;
    private ReleaseResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        release = Release.builder()
                .id(1L)
                .name("Test")
                .status(ReleaseStatus.CREATED)
                .releaseDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .lastUpdateAt(LocalDateTime.now())
                .build();

        responseDTO = ReleaseResponseDTO.builder()
                .id(1L)
                .name("Test")
                .status("Created")
                .build();
    }

    @Test
    void getOne_shouldReturnDTO_whenFound() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
        when(releaseMapper.toDTO(release)).thenReturn(responseDTO);

        ReleaseResponseDTO result = releaseService.getOne(1L);

        assertEquals(1L, result.getId());
        verify(releaseRepository).findById(1L);
    }


    @Test
    void getOne_shouldThrow_whenNotFound() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ReleaseNotFoundException.class,
                () -> releaseService.getOne(1L));
    }

    @Test
    void create_shouldSaveAndReturnDTO() {
        ReleaseRequestDTO request = new ReleaseRequestDTO();
        request.setName("New");
        request.setReleaseDate(LocalDate.now());

        when(releaseMapper.toEntity(request)).thenReturn(release);
        when(releaseRepository.save(release)).thenReturn(release);
        when(releaseMapper.toDTO(release)).thenReturn(responseDTO);

        ReleaseResponseDTO result = releaseService.create(request);

        assertEquals("Test", result.getName());
        verify(releaseRepository).save(release);
    }


    @Test
    void update_shouldNotCallWorkflow_whenStatusNull() {
        ReleaseRequestDTO request = new ReleaseRequestDTO();
        request.setName("Updated");
        request.setReleaseDate(LocalDate.now());

        when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
        when(releaseRepository.save(any())).thenReturn(release);
        when(releaseMapper.toDTO(any())).thenReturn(responseDTO);

        releaseService.update(1L, request);

        verify(workflowService, never()).applyTransition(any(), any());
    }

    @Test
    void update_shouldCallWorkflow_whenStatusProvided() {
        ReleaseRequestDTO request = new ReleaseRequestDTO();
        request.setName("Updated");
        request.setStatus("In Development");
        request.setReleaseDate(LocalDate.now());

        when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
        when(releaseRepository.save(any())).thenReturn(release);
        when(releaseMapper.toDTO(any())).thenReturn(responseDTO);

        releaseService.update(1L, request);

        verify(workflowService).applyTransition(eq(release), eq(ReleaseStatus.IN_DEVELOPMENT));
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        when(releaseRepository.existsById(1L)).thenReturn(false);

        assertThrows(ReleaseNotFoundException.class,
                () -> releaseService.delete(1L));
    }

    @Test
    void filter_shouldReturnMappedList() {
        when(releaseRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(release));
        when(releaseMapper.toDTO(release)).thenReturn(responseDTO);

        List<ReleaseResponseDTO> result =
                releaseService.filter("Test", null, null, null, null);

        assertEquals(1, result.size());
        verify(releaseRepository).findAll(any(Specification.class));
    }

}
