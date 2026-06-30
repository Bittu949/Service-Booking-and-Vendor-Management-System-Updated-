package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "UpdateBookingStatusRequest",
        description = "Request payload for updating the status of a booking."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateBookingStatusRequest {

    @Schema(
            description = "New status to be assigned to the booking.",
            example = "COMPLETED",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private BookingStatus status;
}
