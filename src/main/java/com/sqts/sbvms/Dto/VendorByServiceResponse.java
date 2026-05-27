package com.sqts.sbvms.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorByServiceResponse {
    private Long vendorId;
    private String vendorName;
    private String vendorEmail;
    private Long price;
    private Duration duration;
}
