package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Model.Address;
import com.sqts.sbvms.Model.TimeSlot;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long serviceId;
    @NotNull
    private LocalDate bookingDate;
    @NotNull
    private TimeSlot timeSlot;
    @NotNull
    @Valid
    private Address bookingAddress;
}
