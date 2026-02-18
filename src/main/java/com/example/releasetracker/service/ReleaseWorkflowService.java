package com.example.releasetracker.service;

import com.example.releasetracker.domain.entity.Release;
import com.example.releasetracker.domain.enums.ReleaseStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service responsible for validating and applying release status transitions.
 *
 * Enforces workflow rules defined in {@link ReleaseStatus}.
 */
@Service
public class ReleaseWorkflowService {

    /**
     * Validates whether a release can transition
     * from its current status to the target status.
     *
     * @param current current release entity
     * @param target  target status
     * @throws IllegalArgumentException if transition is not allowed
     */
    public void validateTransition(Release current, ReleaseStatus target) {
        if (!current.getStatus().canTransitionTo(target)) {
            throw new IllegalArgumentException(
                    "Cannot change status from " + current.getStatus().getDisplayName() +
                            " to " + target.getDisplayName() +
                            ". Allowed: " + current.getStatus().allowedNext().stream()
                            .map(ReleaseStatus::getDisplayName).toList()
            );
        }
    }

    /**
     * Applies a valid status transition to a release.
     * Updates the status and last update timestamp.
     *
     * @param release release entity
     * @param target  new status
     */
    public void applyTransition(Release release, ReleaseStatus target) {
        validateTransition(release, target);
        release.setStatus(target);
        release.setLastUpdateAt(LocalDateTime.now());
    }
}

