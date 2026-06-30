package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(
        name = "PendingBookingResponse",
        description = "Details of a booking awaiting vendor assignment."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PendingBookingResponse {

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
            description = "Scheduled service start time.",
            example = "10:00:00"
    )
    private LocalTime startTime;

    @Schema(
            description = "Scheduled service end time.",
            example = "12:00:00"
    )
    private LocalTime endTime;

    @Schema(
            description = "Current status of the booking.",
            example = "PENDING"
    )
    private BookingStatus status;

    @Valid
    @Schema(
            description = "Address where the service will be provided."
    )
    private Address bookingAddress;
}
