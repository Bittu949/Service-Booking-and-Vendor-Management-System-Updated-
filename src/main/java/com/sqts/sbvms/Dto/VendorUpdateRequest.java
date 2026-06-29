package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Model.Address;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorUpdateRequest {
    @NotBlank
    private String vendorName;
    @NotBlank
    @Email
    private String vendorEmail;
    @NotBlank
    private String password;
    @Valid
    private Address vendorAddress;
    @NotBlank
    @Column(unique = true)
    private String phoneNumber;
}
