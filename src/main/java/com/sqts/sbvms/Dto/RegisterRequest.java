package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "RegisterRequest",
        description = "Request payload for registering a new customer account."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    @Schema(
            description = "Full name of the customer.",
            example = "Suraj Patil",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String name;

    @Schema(
            description = "Email address of the customer.",
            example = "suraj@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "Invalid email address")
    @NotBlank
    private String email;

    @Schema(
            description = "Password for the customer account. Must contain at least 8 characters.",
            example = "Suraj@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    @Schema(
            description = "Confirmation of the password entered above.",
            example = "Suraj@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String confirmPassword;
}