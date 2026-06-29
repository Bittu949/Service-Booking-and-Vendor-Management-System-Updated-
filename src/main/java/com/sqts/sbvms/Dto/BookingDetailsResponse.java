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
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDetailsResponse {
    private Long bookingId;
    private LocalDate bookingDate;
    private BookingStatus bookingStatus;
    private LocalTime startTime;
    private LocalTime endTime;
    private String serviceName;
    private String customerName;
    private Long vendorId;
    private String vendorName;
    @Valid
    private Address vendorAddress;
    @Valid
    private Address bookingAddress;
    private Long finalPrice;
    private Duration estimatedDuration;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
}
