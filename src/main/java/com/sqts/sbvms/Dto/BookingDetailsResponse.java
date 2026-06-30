package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.Address;
import com.sqts.sbvms.Model.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(
        name = "BookingDetailsResponse",
        description = "Detailed information about a booking."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDetailsResponse {

    @Schema(
            description = "Unique identifier of the booking.",
            example = "101"
    )
    private Long bookingId;

    @Schema(
            description = "Scheduled date of the booking.",
            example = "2026-07-10"
    )
    private LocalDate bookingDate;

    @Schema(
            description = "Current status of the booking.",
            example = "ASSIGNED"
    )
    private BookingStatus bookingStatus;

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
            description = "Name of the requested service.",
            example = "Electrician"
    )
    private String serviceName;

    @Schema(
            description = "Name of the customer who created the booking.",
            example = "Bittu Naik"
    )
    private String customerName;

    @Schema(
            description = "Unique identifier of the assigned vendor.",
            example = "12"
    )
    private Long vendorId;

    @Schema(
            description = "Name of the assigned vendor.",
            example = "Rahul Sharma"
    )
    private String vendorName;

    @Valid
    @Schema(
            description = "Business address of the assigned vendor."
    )
    private Address vendorAddress;

    @Valid
    @Schema(
            description = "Address where the service will be provided."
    )
    private Address bookingAddress;

    @Schema(
            description = "Final price of the booking.",
            example = "1500"
    )
    private Long finalPrice;

    @Schema(
            description = "Estimated duration required to complete the service.",
            example = "PT2H"
    )
    private Duration estimatedDuration;

    @Schema(
            description = "Date and time when the vendor was assigned.",
            example = "2026-07-08T14:30:00"
    )
    private LocalDateTime assignedAt;

    @Schema(
            description = "Date and time when the booking was marked as completed.",
            example = "2026-07-10T12:05:00"
    )
    private LocalDateTime completedAt;
}