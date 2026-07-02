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
        name = "VendorVerificationResponse",
        description = "Complete vendor details used by an administrator during the vendor verification process."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorVerificationResponse {

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
            example = "Experienced electrician specializing in residential and commercial electrical services."
    )
    private String description;

    @Valid
    @Schema(
            description = "Business address of the vendor."
    )
    private Address vendorAddress;

    @Valid
    @Schema(
            description = "Service requested by the vendor."
    )
    private PendingVendorServiceResponse service;

    @Schema(
            description = "Vendor's Aadhaar number.",
            example = "123412341234"
    )
    private String aadhaarNumber;

    @Schema(
            description = "Public URL of the uploaded Aadhaar front image. Open this URL in a browser to view the document.",
            example = "https://res.cloudinary.com/your-cloud/image/upload/v1782992382/vendor-documents/aadhaar-front/3b0c2f6d-7e81-4f9d-8f8c-9b3f2a1d5abc.jpg"
    )
    private String aadhaarFrontImage;

    @Schema(
            description = "Public URL of the uploaded Aadhaar back image. Open this URL in a browser to view the document.",
            example = "https://res.cloudinary.com/your-cloud/image/upload/v1782992382/vendor-documents/aadhaar-back/7f92d3a8-1d45-4b9d-a5f3-8c4d9b1e2def.jpg"
    )
    private String aadhaarBackImage;

    @Schema(
            description = "Public URL of the uploaded vendor verification document. Open this URL in a browser to view the uploaded document (image or PDF).",
            example = "https://res.cloudinary.com/your-cloud/image/upload/v1782992382/vendor-documents/verification/9a7b5c4d-8f21-4b7e-bd93-4d1f2c8e7abc.pdf"
    )
    private String verificationDocument;

    @Schema(
            description = "Current approval status of the vendor.",
            example = "PENDING"
    )
    private VendorStatus status;
}