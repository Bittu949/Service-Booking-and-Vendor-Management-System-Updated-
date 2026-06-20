package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.ApiResponse;
import com.sqts.sbvms.Dto.RegisterRequest;
import com.sqts.sbvms.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
}
