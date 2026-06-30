package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
@Schema(
        name = "ServiceUpdationRequest",
        description = "Request payload for updating the price or estimated duration of a vendor's assigned service. Only the provided fields will be updated."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceUpdationRequest {

    @Schema(
            description = "Updated service price.",
            example = "1800"
    )
    private Long price;

    @Schema(
            description = "Updated estimated duration required to complete the service.",
            example = "PT3H"
    )
    private Duration duration;
}
