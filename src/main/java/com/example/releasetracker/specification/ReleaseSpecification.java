package com.example.releasetracker.specification;

import com.example.releasetracker.domain.entity.Release;
import com.example.releasetracker.domain.enums.ReleaseStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building dynamic JPA Specifications
 * for filtering {@link Release} entities.
 *
 * Supports filtering by:
 * - name (partial match, case-insensitive)
 * - description (partial match, case-insensitive)
 * - status (exact match)
 * - release date range
 */
public class ReleaseSpecification {

    /**
     * Private constructor to prevent instantiation.
     */
    private ReleaseSpecification() {
    }

    /**
     * Builds a dynamic specification based on optional filter parameters.
     *
     * @param name        partial match for release name
     * @param description partial match for description
     * @param status      exact match for status
     * @param fromDate    minimum release date (inclusive)
     * @param toDate      maximum release date (inclusive)
     * @return specification for querying releases
     */
    public static Specification<Release> filterBy(
            String name,
            String description,
            ReleaseStatus status,
            LocalDate fromDate,
            LocalDate toDate) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            if (description != null && !description.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("description")),
                                "%" + description.toLowerCase() + "%"
                        )
                );
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (fromDate != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("releaseDate"), fromDate)
                );
            }

            if (toDate != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("releaseDate"), toDate)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

