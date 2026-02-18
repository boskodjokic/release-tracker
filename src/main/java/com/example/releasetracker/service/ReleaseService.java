package com.example.releasetracker.service;

import com.example.releasetracker.domain.entity.Release;
import com.example.releasetracker.domain.enums.ReleaseStatus;
import com.example.releasetracker.dto.ReleaseResponseDTO;
import com.example.releasetracker.exception.ReleaseNotFoundException;
import com.example.releasetracker.mapper.ReleaseMapper;
import com.example.releasetracker.specification.ReleaseSpecification;
import com.example.releasetracker.dto.ReleaseRequestDTO;
import com.example.releasetracker.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer responsible for managing {@link Release} entities.
 *
 * Handles CRUD operations and delegates workflow validation
 * to {@link ReleaseWorkflowService}.
 */
@Service
@RequiredArgsConstructor
public class ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ReleaseMapper releaseMapper;
    private final ReleaseWorkflowService releaseWorkflowService;

    private static final Logger log = LoggerFactory.getLogger(ReleaseService.class);

    /**
     * Retrieves a single release by its ID.
     *
     * @param id release ID
     * @return ReleaseResponseDTO representing the release
     * @throws ReleaseNotFoundException if no release exists with given ID
     */
    public ReleaseResponseDTO getOne(Long id) {
        log.info("Fetching release with id={}", id);
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Release with id={} not found", id);
                    return new ReleaseNotFoundException(id);
                });

        return releaseMapper.toDTO(release);
    }

    /**
     * Creates a new release.
     * If status is not provided, it defaults to {@code CREATED}.
     *
     * @param dto request data
     * @return created release as DTO
     */
    public ReleaseResponseDTO create(ReleaseRequestDTO dto) {
        log.info("Creating new release with name={}", dto.getName());

        Release entity = releaseMapper.toEntity(dto);
        Release saved = releaseRepository.save(entity);

        log.info("Release created successfully with id={}", saved.getId());

        return releaseMapper.toDTO(saved);
    }

    /**
     * Updates an existing release.
     *
     * Status transition is validated through {@link ReleaseWorkflowService}.
     * Status update is optional; if not provided, existing status remains unchanged.
     *
     * @param id  ID of release to update
     * @param dto updated data
     * @return updated release as DTO
     * @throws ReleaseNotFoundException if release does not exist
     * @throws IllegalArgumentException if invalid status transition is attempted
     */
    public ReleaseResponseDTO update(Long id, ReleaseRequestDTO dto) {
        log.info("Updating release with id={}", id);

        Release existing = releaseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted update but release with id={} not found", id);
                    return new ReleaseNotFoundException(id);
                });

        ReleaseStatus newStatus = null;
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            newStatus = ReleaseStatus.fromDisplayName(dto.getStatus());
            log.info("Requested status transition for release id={} to {}", id, newStatus);
        }

        if (newStatus != null) {
            releaseWorkflowService.applyTransition(existing, newStatus);
        }

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setReleaseDate(dto.getReleaseDate());

        Release updated = releaseRepository.save(existing);

        log.info("Release with id={} updated successfully", id);

        return releaseMapper.toDTO(updated);
    }

    /**
     * Deletes a release by its ID.
     *
     * @param id release ID
     */
    public void delete(Long id) {
        log.info("Deleting release with id={}", id);

        if (!releaseRepository.existsById(id)) {
            log.warn("Attempted delete but release with id={} not found", id);
            throw new ReleaseNotFoundException(id);
        }

        releaseRepository.deleteById(id);
        log.info("Release with id={} deleted successfully", id);

    }

    /**
     * Filters releases using optional search criteria.
     *
     * @param name        partial match on release name
     * @param description partial match on description
     * @param status      exact match on release status
     * @param fromDate    filter releases from this date (inclusive)
     * @param toDate      filter releases up to this date (inclusive)
     * @return list of matching releases
     */
    public List<ReleaseResponseDTO> filter(
            String name,
            String description,
            ReleaseStatus status,
            LocalDate fromDate,
            LocalDate toDate) {

        log.info("Filtering releases with criteria: name={}, description={}, status={}, fromDate={}, toDate={}", name, description, status, fromDate, toDate);

        Specification<Release> specification =
                ReleaseSpecification.filterBy(name, description, status, fromDate, toDate);

        List<ReleaseResponseDTO> results = releaseRepository.findAll(specification)
                .stream()
                .map(releaseMapper::toDTO)
                .toList();

        log.info("Filter returned {} results", results.size());

        return results;
    }
}



