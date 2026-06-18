package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Model.Address;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorCreationRequest {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @Valid
    @NotNull
    private Address vendorAddress;
}
