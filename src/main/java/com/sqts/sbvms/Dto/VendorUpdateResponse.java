package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Entity.Vendor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorUpdateResponse {
    private Long vendorId;
    private String vendorName;
    private String vendorEmail;
}
