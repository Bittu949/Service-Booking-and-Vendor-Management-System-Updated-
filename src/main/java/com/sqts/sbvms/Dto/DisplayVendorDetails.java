package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DisplayVendorDetails {
    @NotBlank
    private String vendorName;
    @NotBlank
    private String vendorEmail;
    private List<VendorServiceDetails> vendorServiceDetails;
    private VendorStatus vendorStatus;
    @Valid
    private Address vendorAddress;
}
