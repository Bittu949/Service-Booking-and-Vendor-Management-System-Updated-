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
public class AssignVendorResponse {
    private Long bookingId;

    private String customerName;

    private String vendorName;

    private String serviceName;

    private BookingStatus bookingStatus;

    private LocalDate bookingDate;

    private Address vendorAddress;

    private LocalTime startTime;

    private LocalTime endTime;
    @Valid
    private Address bookingAddress;
}
