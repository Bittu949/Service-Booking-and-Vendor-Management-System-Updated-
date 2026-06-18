package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.Role;
import com.sqts.sbvms.Model.Address;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorCreationResponse {
    private Long vendorId;
    private String name;
    private String email;
    private Role role;
    @Valid
    private Address vendorAddress;
}
