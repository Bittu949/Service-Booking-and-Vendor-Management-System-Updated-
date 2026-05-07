package com.sqts.sbvms.Dto;

import com.sqts.sbvms.Model.TimeSlot;
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
    Long userId;
    @NotNull
    Long serviceId;
    @NotNull
    LocalDate bookingDate;
    @NotNull
    TimeSlot timeSlot;
}
