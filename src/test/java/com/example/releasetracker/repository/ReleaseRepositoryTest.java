package com.example.releasetracker.repository;

import com.example.releasetracker.domain.entity.Release;
import com.example.releasetracker.domain.enums.ReleaseStatus;
import com.example.releasetracker.specification.ReleaseSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReleaseRepositoryTest {

    @Autowired
    private ReleaseRepository repository;

    @Test
    void shouldFilterByStatus() {
        Release release = Release.builder()
                .name("RepoTest")
                .status(ReleaseStatus.CREATED)
                .releaseDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .lastUpdateAt(LocalDateTime.now())
                .build();

        repository.save(release);

        List<Release> result =
                repository.findAll(ReleaseSpecification.filterBy(
                        null, null, ReleaseStatus.CREATED, null, null));

        assertEquals(1, result.size());
    }
}
