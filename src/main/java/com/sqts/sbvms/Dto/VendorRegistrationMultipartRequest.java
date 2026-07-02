package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Schema(
        name = "VendorRegistrationMultipartRequest",
        description = "Vendor registration request including uploaded verification documents."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorRegistrationMultipartRequest {

    @NotBlank
    @Schema(example = "Rahul Sharma")
    private String name;

    @NotBlank
    @Email
    @Schema(example = "rahul@gmail.com")
    private String email;

    @NotBlank
    @Schema(example = "Rahul@123")
    private String password;

    @NotBlank
    @Schema(example = "9876543210")
    private String phoneNumber;

    @NotNull
    @Schema(example = "5")
    private Integer experienceYears;

    @Schema(example = "Experienced electrician")
    private String description;

    @NotBlank
    @Schema(example = "123412341234")
    private String aadhaarNumber;

    @NotBlank
    @Schema(example = "Flat 101")
    private String addressLine1;

    @Schema(example = "Near City Mall")
    private String addressLine2;

    @NotBlank
    @Schema(example = "Pune")
    private String city;

    @NotBlank
    @Schema(example = "Maharashtra")
    private String state;

    @NotBlank
    @Schema(example = "411001")
    private String pincode;

    @NotNull
    @Schema(example = "1")
    private Long serviceCategoryId;

    @NotNull
    @Schema(example = "1500")
    private Long price;

    @NotBlank
    @Schema(example = "02:00")
    private String duration;

    @Schema(
            description = "Front image of the vendor's Aadhaar card.",
            type = "string",
            format = "binary",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private MultipartFile aadhaarFrontImage;

    @Schema(
            description = "Back image of the vendor's Aadhaar card.",
            type = "string",
            format = "binary",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private MultipartFile aadhaarBackImage;

    @Schema(
            description = "Additional verification document such as a license, certificate, or government-issued proof. Images and PDF files are supported.",
            type = "string",
            format = "binary",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private MultipartFile verificationDocument;
}