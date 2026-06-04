package com.sqts.sbvms.Exception;

import com.sqts.sbvms.Dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PasswordMismatchedException.class)
    public ResponseEntity<ApiResponse<Object>> handlePasswordMismatchedException(PasswordMismatchedException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidInputException(InvalidInputException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getBindingResult()
                                .getFieldErrors()
                                .getFirst()
                                .getDefaultMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoServiceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoServiceFoundException(NoServiceFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NoVendorFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoVendorFoundException(NoVendorFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidTimeSlotException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidTimeSlotException(InvalidTimeSlotException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(UserNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleServiceNotFoundException(ServiceNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(VendorNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleVendorNotFoundException(VendorNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BookingsNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleBookingsNotFoundException(BookingsNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleBookingNotFoundException(BookingNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidBookingStatusException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidBookingStatusException(InvalidBookingStatusException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidVendorException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidVendorException(InvalidVendorException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<ApiResponse<Object>> handleWeakPasswordException(WeakPasswordException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DuplicateServiceAssignmentException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateServiceAssignmentException(DuplicateServiceAssignmentException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ServiceAssignmentNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleServiceAssignmentNotFoundException(ServiceAssignmentNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidVendorStatusException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidVendorStatusException(InvalidVendorStatusException ex){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null,
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
}
