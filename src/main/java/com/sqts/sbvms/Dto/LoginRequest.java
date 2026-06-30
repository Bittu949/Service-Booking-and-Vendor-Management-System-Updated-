package com.sqts.sbvms.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "LoginRequest",
        description = "Request payload for authenticating a registered customer or approved vendor."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @Schema(
            description = "Registered email address of the user.",
            example = "suraj@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @Email(message = "Invalid email address")
    private String email;

    @Schema(
            description = "Account password.",
            example = "Suraj@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String password;
}
