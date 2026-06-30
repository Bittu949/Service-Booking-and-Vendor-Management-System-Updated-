package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "VendorUpdateRequest",
        description = "Request payload for updating a vendor's profile information."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorUpdateRequest {

    @Schema(
            description = "Updated full name of the vendor.",
            example = "Rahul Sharma",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String vendorName;

    @Schema(
            description = "Updated email address of the vendor.",
            example = "rahul@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @Email
    private String vendorEmail;

    @Schema(
            description = "Updated account password.",
            example = "Rahul@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String password;

    @Schema(
            description = "Updated business address of the vendor.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Valid
    @NotNull
    private Address vendorAddress;

    @Schema(
            description = "Updated contact number of the vendor.",
            example = "9876543210",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String phoneNumber;
}