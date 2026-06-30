package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
@Schema(
        name = "AssignVendorResponse",
        description = "Response returned after successfully assigning a vendor to a booking."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssignVendorResponse {

    @Schema(
            description = "Unique identifier of the booking.",
            example = "101"
    )
    private Long bookingId;

    @Schema(
            description = "Name of the customer who created the booking.",
            example = "Bittu Naik"
    )
    private String customerName;

    @Schema(
            description = "Name of the assigned vendor.",
            example = "Rahul Sharma"
    )
    private String vendorName;

    @Schema(
            description = "Name of the requested service.",
            example = "Electrician"
    )
    private String serviceName;

    @Schema(
            description = "Current status of the booking.",
            example = "ASSIGNED"
    )
    private BookingStatus bookingStatus;

    @Schema(
            description = "Scheduled booking date.",
            example = "2026-07-05"
    )
    private LocalDate bookingDate;

    @Schema(
            description = "Address of the assigned vendor."
    )
    private Address vendorAddress;

    @Schema(
            description = "Scheduled start time of the service.",
            example = "10:00:00"
    )
    private LocalTime startTime;

    @Schema(
            description = "Scheduled end time of the service.",
            example = "12:00:00"
    )
    private LocalTime endTime;

    @Valid
    @Schema(
            description = "Address where the service will be provided."
    )
    private Address bookingAddress;

    @Schema(
            description = "Final price agreed for the booking.",
            example = "1500"
    )
    private Long finalPrice;

    @Schema(
            description = "Estimated duration required to complete the service.",
            example = "PT2H"
    )
    private Duration estimatedDuration;
}