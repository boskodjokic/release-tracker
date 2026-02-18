package com.example.releasetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReleaseRequestDTO {

    @NotBlank
    @Schema(description = "Name of the release", example = "My Release v1")
    private String name;

    @Schema(description = "Detailed description of the release", example = "This release includes new features and bug fixes.")
    private String description;

    @Schema(
            description = "Status of the release",
            allowableValues = {
                    "Created",
                    "In Development",
                    "On DEV",
                    "QA Done on DEV",
                    "On Staging",
                    "QA Done on STAGING",
                    "On PROD",
                    "Done"
            },
            example = "In Development"
    )
    private String status;

    @NotNull
    @Schema(description = "Release date", example = "2026-02-17")
    private LocalDate releaseDate;

    public String getStatus() {
        return status;
    }
}
