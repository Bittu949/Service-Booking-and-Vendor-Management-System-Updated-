package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PendingVendorResponse {
    private Long vendorId;
    private String vendorName;
    private String email;
    private String phoneNumber;
    private Integer experienceYears;
    private String description;
    private Address vendorAddress;
    private VendorStatus status;
}
