package com.sqts.sbvms.Model;

import com.sqts.sbvms.Exception.InvalidTimeSlotException;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TimeSlot {
    LocalTime startTime;
    LocalTime endTime;
    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeSlotException("Start time must be before end time");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

