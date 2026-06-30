package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "AssignVendorRequest",
        description = "Request payload for assigning a vendor service to a booking."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssignVendorRequest {

    @Schema(
            description = "Unique identifier of the vendor service to be assigned.",
            example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Long vendorServiceId;
}
