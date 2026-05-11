package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Entity.Vendor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceAssignmentResponse {
    @NotNull
    private Long vendorId;
    @NotBlank
    private String serviceCategoryName;
    @NotNull
    private Long price;
    @NotNull
    private Duration duration;
}
