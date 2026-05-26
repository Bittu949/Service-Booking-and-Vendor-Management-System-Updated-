package com.sqts.sbvms.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceUpdationRequest {
    private Long price;
    private Duration duration;
}
