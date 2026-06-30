package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "ServiceCategoryUpdateRequest",
        description = "Request payload for updating an existing service category. Only the provided fields will be updated."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceCategoryUpdateRequest {

    @Schema(
            description = "Updated name of the service category.",
            example = "Plumbing"
    )
    private String serviceName;

    @Schema(
            description = "Updated description of the service category.",
            example = "Installation, repair and maintenance of water supply and drainage systems."
    )
    private String description;
}
