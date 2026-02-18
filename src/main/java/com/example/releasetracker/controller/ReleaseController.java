package com.example.releasetracker.controller;

import com.example.releasetracker.domain.enums.ReleaseStatus;
import com.example.releasetracker.dto.ReleaseResponseDTO;
import com.example.releasetracker.dto.ReleaseRequestDTO;
import com.example.releasetracker.service.ReleaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/releases")
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseService releaseService;

    @GetMapping
    @Operation(summary = "List and filter releases")
    public ResponseEntity<List<ReleaseResponseDTO>> list(
            @Parameter(description = "Filter by release name (partial match)", example = "Release v1")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filter by description", example = "features")
            @RequestParam(required = false) String description,

            @Parameter(description = "Filter by release status", example = "In Development")
            @RequestParam(required = false) ReleaseStatus status,

            @Parameter(description = "Filter releases from this date", example = "2026-02-01")
            @RequestParam(required = false) LocalDate fromDate,

            @Parameter(description = "Filter releases up to this date", example = "2026-02-28")
            @RequestParam(required = false) LocalDate toDate
    ) {
        return ResponseEntity.ok(releaseService.filter(name, description, status, fromDate, toDate));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single release by ID")
    public ResponseEntity<ReleaseResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(releaseService.getOne(id));
    }

    @PostMapping
    @Operation(summary = "Create new release")
    public ResponseEntity<ReleaseResponseDTO> create(@Valid @RequestBody ReleaseRequestDTO dto) {
        ReleaseResponseDTO created = releaseService.create(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing release")
    public ResponseEntity<ReleaseResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ReleaseRequestDTO dto
    ) {
        return ResponseEntity.ok(releaseService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a release by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        releaseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



