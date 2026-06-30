package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "VendorRegistrationResponse",
        description = "Response returned after successfully registering a vendor."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorRegistrationResponse {

    @Schema(
            description = "Unique identifier of the registered vendor.",
            example = "15"
    )
    private Long vendorId;

    @Schema(
            description = "Full name of the registered vendor.",
            example = "Rahul Sharma"
    )
    private String vendorName;

    @Schema(
            description = "Current approval status of the vendor after registration.",
            example = "PENDING"
    )
    private VendorStatus status;
}