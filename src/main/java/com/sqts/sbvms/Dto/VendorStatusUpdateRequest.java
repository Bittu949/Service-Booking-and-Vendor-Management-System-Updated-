package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.VendorStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "VendorStatusUpdateRequest",
        description = "Request payload for updating the approval status of a vendor."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorStatusUpdateRequest {

    @Schema(
            description = "New approval status to be assigned to the vendor.",
            example = "ACTIVE",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private VendorStatus status;
}
