package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(
        name = "AdminBookingHistoryResponse",
        description = "Complete booking details returned for administrator."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminBookingHistoryResponse {

    @Schema(example = "101")
    private Long bookingId;

    @Schema(example = "2026-07-10")
    private LocalDate bookingDate;

    @Schema(example = "CONFIRMED")
    private BookingStatus bookingStatus;

    @Schema(example = "10:00:00")
    private LocalTime startTime;

    @Schema(example = "12:00:00")
    private LocalTime endTime;

    @Schema(example = "Electrician")
    private String serviceName;

    @Schema(example = "5")
    private Long customerId;

    @Schema(example = "Suraj Patil")
    private String customerName;

    @Schema(example = "suraj@gmail.com")
    private String customerEmail;

    @Schema(example = "12")
    private Long vendorId;

    @Schema(example = "Rahul Sharma")
    private String vendorName;

    @Schema(example = "rahul@gmail.com")
    private String vendorEmail;

    @Valid
    private Address bookingAddress;

    @Valid
    private Address vendorAddress;

    @Schema(example = "1500")
    private Long finalPrice;

    @Schema(example = "02:00")
    private String estimatedDuration;

    @Schema(example = "2026-07-08T14:30:00")
    private LocalDateTime assignedAt;

    @Schema(example = "2026-07-10T12:05:00")
    private LocalDateTime completedAt;
}