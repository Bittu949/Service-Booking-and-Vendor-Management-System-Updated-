package com.sqts.sbvms.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceAssignmentRequest {
    @NotNull
    private Long vendorId;
    @NotNull
    private Long serviceCategoryId;
    @NotNull
    @Positive
    private Long price;
    @NotNull
    Duration duration;
}
