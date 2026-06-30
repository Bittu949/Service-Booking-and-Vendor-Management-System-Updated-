package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.Address;
import com.sqts.sbvms.Model.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Schema(
        name = "BookingResponse",
        description = "Response returned after successfully creating a booking."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingResponse {

    @Schema(
            description = "Unique identifier of the booking.",
            example = "101"
    )
    private Long bookingId;

    @Schema(
            description = "Name of the customer who created the booking.",
            example = "Suraj Patil"
    )
    private String customerName;

    @Schema(
            description = "Name of the requested service.",
            example = "Electrician"
    )
    private String serviceName;

    @Schema(
            description = "Scheduled booking date.",
            example = "2026-07-10"
    )
    private LocalDate bookingDate;

    @Schema(
            description = "Scheduled time slot for the booking."
    )
    @Valid
    private TimeSlot timeSlot;

    @Schema(
            description = "Current status of the booking.",
            example = "PENDING"
    )
    private BookingStatus status;

    @Schema(
            description = "Address where the service will be provided."
    )
    @Valid
    private Address bookingAddress;
}
