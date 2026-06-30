package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "VendorRegistrationRequest",
        description = "Request payload for registering a new vendor account. Newly registered vendors remain pending until approved by an administrator."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorRegistrationRequest {

    @Schema(
            description = "Full name of the vendor.",
            example = "Rahul Sharma",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String name;

    @Schema(
            description = "Email address of the vendor.",
            example = "rahul@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @Email
    private String email;

    @Schema(
            description = "Password for the vendor account.",
            example = "Rahul@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String password;

    @Schema(
            description = "Business address of the vendor.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Valid
    @NotNull
    private Address vendorAddress;

    @Schema(
            description = "Vendor's contact number.",
            example = "9876543210",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String phoneNumber;

    @Schema(
            description = "Total years of professional experience.",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Integer experienceYears;

    @Schema(
            description = "Brief description of the vendor's skills and experience.",
            example = "Experienced electrician specializing in residential and commercial electrical services."
    )
    private String description;

    @Schema(
            description = "12-digit Aadhaar number of the vendor.",
            example = "123412341234",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String aadhaarNumber;

    @Schema(
            description = "URL or path of the uploaded Aadhaar front image.",
            example = "aadhaar_front.jpg",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String aadhaarFrontImage;

    @Schema(
            description = "URL or path of the uploaded Aadhaar back image.",
            example = "aadhaar_back.jpg",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String aadhaarBackImage;

    @Schema(
            description = "URL or path of an additional verification document.",
            example = "electrician_license.pdf",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String verificationDocument;
}