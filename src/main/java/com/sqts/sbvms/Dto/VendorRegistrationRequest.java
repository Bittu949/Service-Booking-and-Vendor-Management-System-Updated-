package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Model.Address;
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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorRegistrationRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @Valid
    private Address vendorAddress;
    @NotBlank
    @Column(unique = true)
    private String phoneNumber;
    @NotNull
    private Integer experienceYears;
    private String description;
    @NotBlank
    private String aadhaarNumber;
    @NotBlank
    private String aadhaarFrontImage;
    @NotBlank
    private String aadhaarBackImage;
    @NotBlank
    private String verificationDocument;
}
