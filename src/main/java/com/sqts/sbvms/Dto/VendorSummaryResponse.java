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
        name = "VendorSummaryResponse",
        description = "Summary information about a vendor."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorSummaryResponse {

    @Schema(
            description = "Unique identifier of the vendor.",
            example = "12"
    )
    private Long vendorId;

    @Schema(
            description = "Full name of the vendor.",
            example = "Rahul Sharma"
    )
    private String vendorName;

    @Schema(
            description = "Email address of the vendor.",
            example = "rahul@gmail.com"
    )
    private String vendorEmail;

    @Schema(
            description = "Total number of services currently assigned to the vendor.",
            example = "5"
    )
    private Long totalAssignedServices;

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
