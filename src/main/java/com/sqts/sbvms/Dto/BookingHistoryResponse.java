package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.Address;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingHistoryResponse {
    private Long bookingId;
    private LocalDate bookingDate;
    private BookingStatus bookingStatus;
    private LocalTime startTime;
    private LocalTime endTime;
    private String serviceName;
    private Long vendorId;
    private String vendorName;
    @Valid
    private Address bookingAddress;
    @Valid
    private Address vendorAddress;
}
