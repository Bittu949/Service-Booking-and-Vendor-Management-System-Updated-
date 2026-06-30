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
        name = "PendingVendorResponse",
        description = "Details of a vendor awaiting administrator approval."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PendingVendorResponse {

    @Schema(
            description = "Unique identifier of the vendor.",
            example = "15"
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
    private String email;

    @Schema(
            description = "Contact number of the vendor.",
            example = "9876543210"
    )
    private String phoneNumber;

    @Schema(
            description = "Years of professional experience.",
            example = "5"
    )
    private Integer experienceYears;

    @Schema(
            description = "Brief description of the vendor's skills and experience.",
            example = "Experienced electrician specializing in residential and commercial wiring."
    )
    private String description;

    @Valid
    @Schema(
            description = "Business address of the vendor."
    )
    private Address vendorAddress;

    @Schema(
            description = "Current approval status of the vendor.",
            example = "PENDING"
    )
    private VendorStatus status;
}
