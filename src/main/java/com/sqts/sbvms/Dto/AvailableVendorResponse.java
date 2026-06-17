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
public class AvailableVendorResponse {
    private Long vendorId;
    private String vendorName;
    private String vendorEmail;
    private Long price;
    private VendorStatus status;
    private Address vendorAddress;
}
