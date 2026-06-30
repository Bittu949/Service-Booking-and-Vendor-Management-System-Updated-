package com.sqts.sbvms.Model;

import com.sqts.sbvms.Exception.InvalidTimeSlotException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Schema(
        name = "TimeSlot",
        description = "Represents the scheduled start and end time of a booking."
)
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TimeSlot {

    @Schema(
            description = "Service start time.",
            example = "10:00:00"
    )
    private LocalTime startTime;

    @Schema(
            description = "Service end time.",
            example = "12:00:00"
    )
    private LocalTime endTime;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeSlotException("Start time must be before end time");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

