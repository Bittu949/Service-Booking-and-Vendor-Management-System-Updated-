package com.sqts.sbvms.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank
    String name;
    @Email(message = "Invalid email address")
    @NotBlank
    String email;
    @NotBlank
    String password;
    @NotBlank
    String confirmPassword;
}
