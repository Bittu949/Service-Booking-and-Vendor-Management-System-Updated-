package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Model.Address;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorSummaryResponse {
    private Long vendorId;
    private String vendorName;
    private String vendorEmail;
    private Long totalAssignedServices;
    private VendorStatus status;
    @Valid
    private Address vendorAddress;
}
