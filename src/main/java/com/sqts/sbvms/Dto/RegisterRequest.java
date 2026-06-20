package com.sqts.sbvms.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    String name;
    @Email(message = "Invalid email address")
    @NotBlank
    String email;
    @NotBlank
    @Size(min = 8, message = "Password must contain at least 8 characters")
    String password;
    @NotBlank
    String confirmPassword;
}
