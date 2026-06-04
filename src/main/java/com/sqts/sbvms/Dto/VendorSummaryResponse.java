package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private VendorStatus status;
}
