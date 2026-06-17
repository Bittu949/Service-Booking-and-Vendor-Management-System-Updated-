package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
}
