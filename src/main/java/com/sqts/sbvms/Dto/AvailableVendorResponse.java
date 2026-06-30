package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "AvailableVendorResponse",
        description = "Details of an available vendor who can be assigned to a booking."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AvailableVendorResponse {

    @Schema(
            description = "Unique identifier of the vendor.",
            example = "12"
    )
    private Long vendorId;

    @Schema(
            description = "Name of the vendor.",
            example = "Rahul Sharma"
    )
    private String vendorName;

    @Schema(
            description = "Email address of the vendor.",
            example = "rahul@gmail.com"
    )
    private String vendorEmail;

    @Schema(
            description = "Price charged by the vendor for the requested service.",
            example = "1200"
    )
    private Long price;

    @Schema(
            description = "Current approval status of the vendor.",
            example = "ACTIVE"
    )
    private VendorStatus status;

    @Valid
    @Schema(
            description = "Business address of the vendor."
    )
    private Address vendorAddress;
}
