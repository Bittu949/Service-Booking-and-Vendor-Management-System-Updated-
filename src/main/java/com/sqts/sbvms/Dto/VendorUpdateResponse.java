package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "VendorUpdateResponse",
        description = "Response returned after successfully updating a vendor's profile."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorUpdateResponse {

    @Schema(
            description = "Unique identifier of the vendor.",
            example = "12"
    )
    private Long vendorId;

    @Schema(
            description = "Updated full name of the vendor.",
            example = "Rahul Sharma"
    )
    private String vendorName;

    @Schema(
            description = "Updated email address of the vendor.",
            example = "rahul@gmail.com"
    )
    private String vendorEmail;

    @Valid
    @Schema(
            description = "Updated business address of the vendor."
    )
    private Address vendorAddress;

    @Schema(
            description = "Updated contact number of the vendor.",
            example = "9876543210"
    )
    private String phoneNumber;
}