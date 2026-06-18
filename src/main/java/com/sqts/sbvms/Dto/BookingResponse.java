package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.Address;
import com.sqts.sbvms.Model.TimeSlot;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingResponse {
    private Long bookingId;
    private String customerName;
    private String serviceName;
    private LocalDate bookingDate;
    private TimeSlot timeSlot;
    private BookingStatus status;
    @Valid
    private Address bookingAddress;
}
