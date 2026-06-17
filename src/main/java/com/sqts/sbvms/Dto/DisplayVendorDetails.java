package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
}
