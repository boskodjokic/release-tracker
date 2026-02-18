package com.example.releasetracker.mapper;

import com.example.releasetracker.domain.entity.Release;
import com.example.releasetracker.domain.enums.ReleaseStatus;
import com.example.releasetracker.dto.ReleaseRequestDTO;
import com.example.releasetracker.dto.ReleaseResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReleaseMapper {

    public Release toEntity(ReleaseRequestDTO dto) {
        return Release.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(dto.getStatus() != null
                        ? ReleaseStatus.fromDisplayName(dto.getStatus())
                        : ReleaseStatus.CREATED)
                .releaseDate(dto.getReleaseDate())
                .createdAt(LocalDateTime.now())
                .lastUpdateAt(LocalDateTime.now())
                .build();
    }

    public ReleaseResponseDTO toDTO(Release entity) {
        return ReleaseResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus().getDisplayName())
                .releaseDate(entity.getReleaseDate())
                .createdAt(entity.getCreatedAt())
                .lastUpdatedAt(entity.getLastUpdateAt())
                .build();
    }
}
