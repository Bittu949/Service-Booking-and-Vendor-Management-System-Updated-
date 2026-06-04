package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorStatusUpdateRequest {
    @NotNull
    private VendorStatus status;
}
