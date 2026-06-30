package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        description = "APIs for customer registration and user authentication."
)
@RestController
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @Operation(
            summary = "Register Customer",
            description = "Registers a new customer account using the provided personal details. "
                    + "A successful registration allows the customer to log in and book services."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Customer registered successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid registration details."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already registered.")
    })
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
    @Operation(
            summary = "User Login",
            description = "Authenticates a registered user or approved vendor and returns a JWT access token. "
                    + "The token must be included in the Authorization header to access secured APIs."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful. JWT token returned."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid email or password."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Vendor account is not approved.")
    })
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
    @Operation(
            summary = "Register as Vendor",
            description = "Allows a new vendor to submit a registration request. The registration remains in pending approval status until reviewed by an administrator."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Vendor registration submitted successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or duplicate email/phone number."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Vendor already exists.")
    })
    @PostMapping("/vendor/register")
    public ResponseEntity<ApiResponse<VendorRegistrationResponse>> registerVendor(
            @Valid @RequestBody VendorRegistrationRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Registration request submitted successfully. Please wait for admin approval.",
                        authService.registerVendor(request),
                        LocalDateTime.now()
                ),
                HttpStatus.CREATED
        );
    }
}
