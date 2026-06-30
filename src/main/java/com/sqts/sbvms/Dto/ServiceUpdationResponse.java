package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Schema(
        name = "ServiceUpdationResponse",
        description = "Response returned after successfully updating the price or estimated duration of a vendor's assigned service."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceUpdationResponse {

    @Schema(
            description = "Name of the vendor.",
            example = "Rahul Sharma"
    )
    private String vendorName;

    @Schema(
            description = "Name of the updated service.",
            example = "Electrician"
    )
    private String serviceName;

    @Schema(
            description = "Updated price charged by the vendor for the service.",
            example = "1800"
    )
    private Long price;

    @Schema(
            description = "Updated estimated duration required to complete the service.",
            example = "PT3H"
    )
    private Duration duration;
}
