package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Schema(
        name = "DisplayVendorDetails",
        description = "Detailed information about a vendor, including profile, assigned services and current status."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DisplayVendorDetails {

    @Schema(
            description = "Full name of the vendor.",
            example = "Rahul Sharma"
    )
    @NotBlank
    private String vendorName;

    @Schema(
            description = "Email address of the vendor.",
            example = "rahul@gmail.com"
    )
    @NotBlank
    private String vendorEmail;

    @Schema(
            description = "List of services currently offered by the vendor."
    )
    private List<VendorServiceDetails> vendorServiceDetails;

    @Schema(
            description = "Current approval status of the vendor.",
            example = "ACTIVE"
    )
    private VendorStatus vendorStatus;

    @Valid
    @Schema(
            description = "Business address of the vendor."
    )
    private Address vendorAddress;
}
