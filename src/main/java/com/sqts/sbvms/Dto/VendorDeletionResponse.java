package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "VendorDeletionResponse",
        description = "Response returned after successfully deleting a vendor."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorDeletionResponse {

    @Schema(
            description = "Name of the deleted vendor.",
            example = "Rahul Sharma"
    )
    private String vendorName;

    @Schema(
            description = "Email address of the deleted vendor.",
            example = "rahul@gmail.com"
    )
    private String vendorEmail;
}
