package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.Address;
import com.sqts.sbvms.Model.TimeSlot;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorBookingHistoryResponse {
    private Long bookingId;
    private String customerName;
    private String serviceName;
    private LocalDate date;
    private TimeSlot timeSlot;
    private BookingStatus bookingStatus;
    @Valid
    private Address bookingAddress;
    private Long finalPrice;
    private Duration estimatedDuration;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
}
