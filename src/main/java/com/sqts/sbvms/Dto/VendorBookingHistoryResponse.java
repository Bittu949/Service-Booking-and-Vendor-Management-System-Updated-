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

@Schema(
        name = "VendorBookingHistoryResponse",
        description = "Booking details returned in a vendor's booking history."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorBookingHistoryResponse {

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
            description = "Name of the booked service.",
            example = "Electrician"
    )
    private String serviceName;

    @Schema(
            description = "Scheduled booking date.",
            example = "2026-07-10"
    )
    private LocalDate date;

    @Valid
    @Schema(
            description = "Scheduled time slot for the booking."
    )
    private TimeSlot timeSlot;

    @Schema(
            description = "Current status of the booking.",
            example = "COMPLETED"
    )
    private BookingStatus bookingStatus;

    @Valid
    @Schema(
            description = "Address where the service will be provided."
    )
    private Address bookingAddress;

    @Schema(
            description = "Final price charged for the booking.",
            example = "1500"
    )
    private Long finalPrice;

    @Schema(
            description = "Estimated duration required to complete the service.",
            example = "PT2H"
    )
    private Duration estimatedDuration;

    @Schema(
            description = "Date and time when the vendor was assigned to the booking.",
            example = "2026-07-08T14:30:00"
    )
    private LocalDateTime assignedAt;

    @Schema(
            description = "Date and time when the booking was marked as completed.",
            example = "2026-07-10T12:05:00"
    )
    private LocalDateTime completedAt;
}