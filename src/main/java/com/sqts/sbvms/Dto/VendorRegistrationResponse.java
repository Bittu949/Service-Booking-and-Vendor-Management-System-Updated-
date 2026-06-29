package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
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
public class VendorRegistrationResponse {
    private Long vendorId;
    private String vendorName;
    private VendorStatus status;
}
