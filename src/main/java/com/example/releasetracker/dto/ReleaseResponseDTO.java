package com.example.releasetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ReleaseResponseDTO {

    @Schema(description = "Release ID", example = "1")
    private Long id;

    @Schema(description = "Name of the release", example = "My Release v1")
    private String name;

    @Schema(description = "Description of the release", example = "This release includes new features and bug fixes.")
    private String description;

    @Schema(description = "Status of the release", example = "In Development")
    private String status; // show display name

    @Schema(description = "Release date", example = "2026-02-17")
    private LocalDate releaseDate;

    @Schema(description = "Timestamp when release was created", example = "2026-02-15T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp of last update", example = "2026-02-16T14:00:00")
    private LocalDateTime lastUpdatedAt;
}
