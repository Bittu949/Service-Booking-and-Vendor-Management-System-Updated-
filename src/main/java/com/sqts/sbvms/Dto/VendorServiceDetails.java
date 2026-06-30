package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Entity.ServiceCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Schema(
        name = "VendorServiceDetails",
        description = "Details of a service currently assigned to a vendor."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorServiceDetails {

    @Schema(
            description = "Price charged by the vendor for the service.",
            example = "1500"
    )
    private Long price;

    @Schema(
            description = "Estimated duration required to complete the service.",
            example = "PT2H"
    )
    private Duration duration;

    @Schema(
            description = "Service category assigned to the vendor."
    )
    private ServiceCategory serviceCategory;
}
