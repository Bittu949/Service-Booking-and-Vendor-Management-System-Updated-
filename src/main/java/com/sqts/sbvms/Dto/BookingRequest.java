package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Model.Address;
import com.sqts.sbvms.Model.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Schema(
        name = "BookingRequest",
        description = "Request payload for creating a new service booking."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingRequest {

    @Schema(
            description = "Unique identifier of the service category to be booked.",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Long serviceId;

    @Schema(
            description = "Preferred date for the service booking.",
            example = "2026-07-10",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private LocalDate bookingDate;

    @Schema(
            description = "Preferred time slot for the booking.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Valid
    private TimeSlot timeSlot;

    @Schema(
            description = "Address where the service should be provided.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Valid
    private Address bookingAddress;
}
