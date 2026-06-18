package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Entity.Vendor;
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
public class VendorUpdateResponse {
    private Long vendorId;
    private String vendorName;
    private String vendorEmail;
    @Valid
    private Address vendorAddress;
}
