package com.sqts.sbvms.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
}
