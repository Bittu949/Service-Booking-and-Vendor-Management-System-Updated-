package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "ServiceCategoryResponse",
        description = "Details of a service category."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceCategoryResponse {

    @Schema(
            description = "Unique identifier of the service category.",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the service category.",
            example = "Electrician"
    )
    private String serviceName;

    @Schema(
            description = "Brief description of the service category.",
            example = "Installation, repair and maintenance of electrical systems."
    )
    private String description;
}
