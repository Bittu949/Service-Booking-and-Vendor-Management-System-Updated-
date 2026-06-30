package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Schema(
        name = "ApiResponse",
        description = "Standard response wrapper returned by all API endpoints."
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    @Schema(
            description = "Indicates whether the request was processed successfully.",
            example = "true"
    )
    private boolean success;

    @Schema(
            description = "Human-readable message describing the result of the request.",
            example = "Booking created successfully."
    )
    private String message;

    @Schema(
            description = "Response payload containing the requested resource or operation result."
    )
    private T data;

    @Schema(
            description = "Date and time when the response was generated.",
            example = "2026-06-30T15:45:20"
    )
    private LocalDateTime timestamp;
}