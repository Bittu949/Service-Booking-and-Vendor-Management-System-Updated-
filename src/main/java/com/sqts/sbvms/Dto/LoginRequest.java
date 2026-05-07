package com.sqts.sbvms.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    @Email(message = "Invalid email address")
    String email;
    @NotBlank
    String password;
}
