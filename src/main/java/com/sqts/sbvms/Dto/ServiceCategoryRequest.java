package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "ServiceCategoryRequest",
        description = "Request payload for creating a new service category."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceCategoryRequest {

    @Schema(
            description = "Name of the service category.",
            example = "Electrician",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String serviceName;

    @Schema(
            description = "Brief description of the service category.",
            example = "Installation, repair and maintenance of electrical systems.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String description;
}
