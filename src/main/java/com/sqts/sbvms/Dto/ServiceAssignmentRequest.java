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
        name = "ServiceAssignmentRequest",
        description = "Request payload for assigning a service category to a vendor with a price and estimated duration."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceAssignmentRequest {

    @Schema(
            description = "Unique identifier of the vendor.",
            example = "12",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Long vendorId;

    @Schema(
            description = "Unique identifier of the service category.",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Long serviceCategoryId;

    @Schema(
            description = "Price charged by the vendor for the service.",
            example = "1500",
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
