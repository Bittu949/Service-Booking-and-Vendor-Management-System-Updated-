package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "UpdateBookingStatusResponse",
        description = "Response returned after successfully updating the status of a booking."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateBookingStatusResponse {

    @Schema(
            description = "Unique identifier of the booking.",
            example = "101"
    )
    private Long bookingId;

    @Schema(
            description = "Previous status of the booking before the update.",
            example = "ASSIGNED"
    )
    private BookingStatus previousStatus;

    @Schema(
            description = "Current status of the booking after the update.",
            example = "COMPLETED"
    )
    private BookingStatus currentStatus;
}
