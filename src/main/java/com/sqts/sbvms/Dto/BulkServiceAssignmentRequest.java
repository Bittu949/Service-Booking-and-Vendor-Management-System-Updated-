package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Schema(
        name = "BulkServiceAssignmentRequest",
        description = "Request payload for assigning a service category with the same price and duration to multiple vendors."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BulkServiceAssignmentRequest {

    @Schema(
            description = "Unique identifier of the service category to assign.",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Long serviceCategoryId;

    @Schema(
            description = "Service price charged by each vendor.",
            example = "1200",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Positive
    private Long price;

    @Schema(
            description = "Estimated duration required to complete the service.",
            example = "PT2H",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Duration duration;
}
