package com.example.releasetracker.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ReleaseStatus {
    CREATED("Created"),
    IN_DEVELOPMENT("In Development"),
    ON_DEV("On DEV"),
    QA_DONE_ON_DEV("QA Done on DEV"),
    ON_STAGING("On Staging"),
    QA_DONE_ON_STAGING("QA Done on STAGING"),
    ON_PROD("On PROD"),
    DONE("Done");

    private final String displayName;


    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    // Map of allowed transitions
    private static final Map<ReleaseStatus, List<ReleaseStatus>> transitions = Map.of(
            CREATED, List.of(IN_DEVELOPMENT),
            IN_DEVELOPMENT, List.of(CREATED, ON_DEV),
            ON_DEV, List.of(IN_DEVELOPMENT, QA_DONE_ON_DEV),
            QA_DONE_ON_DEV, List.of(ON_DEV, ON_STAGING),
            ON_STAGING, List.of(QA_DONE_ON_DEV, QA_DONE_ON_STAGING),
            QA_DONE_ON_STAGING, List.of(ON_STAGING, ON_PROD),
            ON_PROD, List.of(QA_DONE_ON_STAGING, DONE),
            DONE, List.of(ON_PROD)
    );

    ReleaseStatus(String displayName) {
        this.displayName = displayName;
    }


    public boolean canTransitionTo(ReleaseStatus target) {
        return target != null && transitions.getOrDefault(this, List.of()).contains(target);
    }

    public List<ReleaseStatus> allowedNext() {
        return Collections.unmodifiableList(transitions.getOrDefault(this, List.of()));
    }

    public static @NonNull ReleaseStatus fromDisplayName(@NonNull String name) {
        return Arrays.stream(values())
                .filter(s -> s.displayName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid status: " + name + ". Valid values: " +
                                Arrays.stream(values()).map(ReleaseStatus::getDisplayName).toList()
                ));
    }

    @JsonCreator
    public static ReleaseStatus fromJson(String value) {
        return fromDisplayName(value);
    }
}