package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @PostMapping("/bookings")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@RequestBody BookingRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Booking created successfully.",
                        bookingService.createBooking(request),
                        LocalDateTime.now()),
                HttpStatus.CREATED);
    }
    @GetMapping("/bookings/pending")
    public ResponseEntity<ApiResponse<List<PendingBookingResponse>>> getPendingBookings(){
        List<PendingBookingResponse> pendingBookings = bookingService.getPendingBookings();
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        pendingBookings.isEmpty() ? "No pending bookings at the moment" : "Bookings found.",
                        pendingBookings,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/bookings/{bookingId}/available-vendors")
    public ResponseEntity<ApiResponse<List<AvailableVendorResponse>>> getAvailableVendorsForBooking(@PathVariable(name = "bookingId") Long bookingId){
        List<AvailableVendorResponse> availableVendors = bookingService.getAvailableVendorsForBooking(bookingId);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        availableVendors.isEmpty() ? "No vendor available at the moment" : "Vendors found.",
                        availableVendors,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
}
