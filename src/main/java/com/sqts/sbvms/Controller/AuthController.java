package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.ApiResponse;
import com.sqts.sbvms.Dto.LoginRequest;
import com.sqts.sbvms.Dto.RegisterRequest;
import com.sqts.sbvms.Service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@Tag(
        name = "Authentication",
        description = "APIs for user registration, vendor registration and login."
)
@RestController
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "User registered successfully.",
                        authService.register(request),
                        LocalDateTime.now()),
                HttpStatus.CREATED);
    }
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody @Valid LoginRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "User logged-in successfully.",
                        authService.login(request),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
}
