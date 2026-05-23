package com.sqts.sbvms.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorDeletionResponse {
    private String vendorName;
    private String vendorEmail;
}
