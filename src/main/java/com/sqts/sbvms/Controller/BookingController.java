package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @GetMapping("/me/bookings/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDetailsResponse>> getMyBookingById(
            @PathVariable Long bookingId){

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Booking found.",
                        bookingService.getMyBookingById(bookingId),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
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
    public ResponseEntity<ApiResponse<List<AvailableVendorResponse>>> getAvailableVendorsForBooking(@PathVariable(name = "bookingId")
                                                                                                        Long bookingId,
                                                                                                    @RequestParam(name = "nearby",
                                                                                                            required = false,
                                                                                                            defaultValue = "false")
                                                                                                    boolean nearby){
        List<AvailableVendorResponse> availableVendors = bookingService.getAvailableVendorsForBooking(bookingId, nearby);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        availableVendors.isEmpty() ? "No vendor available at the moment" : "Vendors found.",
                        availableVendors,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @PatchMapping("/bookings/{id}/assign-vendor")
    public ResponseEntity<ApiResponse<AssignVendorResponse>> assignVendorToBooking(@RequestBody AssignVendorRequest request,
                                                                                   @PathVariable(name = "id") Long bookingId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor assigned to booking.",
                        bookingService.assignVendorToBooking(request, bookingId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/bookings/{id}")
    public ResponseEntity<ApiResponse<BookingDetailsResponse>> getBookingById(@PathVariable(name = "id") Long bookingId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Booking found.",
                        bookingService.getBookingById(bookingId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/users/{id}/bookings")
    public ResponseEntity<ApiResponse<List<BookingHistoryResponse>>> getCustomerBookingHistory(@PathVariable(name = "id") Long customerId){
        List<BookingHistoryResponse> bookingHistory = bookingService.getCustomerBookingHistory(customerId);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        !bookingHistory.isEmpty() ? "Bookings found." : "Bookings not found.",
                        bookingHistory,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<ApiResponse<UpdateBookingStatusResponse>> updateBookingStatus(@RequestBody UpdateBookingStatusRequest request,
                                                                                        @PathVariable(name = "id") Long bookingId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Booking status updated.",
                        bookingService.updateBookingStatus(bookingId, request),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/vendors/{id}/bookings")
    public ResponseEntity<ApiResponse<List<VendorBookingHistoryResponse>>> getVendorBookingHistory(@PathVariable(name = "id") Long vendorId){
        List<VendorBookingHistoryResponse> vendorBookings = bookingService.getVendorBookingHistory(vendorId);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        !vendorBookings.isEmpty() ? "Booking found." : "Bookings not found",
                        vendorBookings,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingHistoryResponse>>> getFilteredBookings(@RequestParam(name = "status", required = false) BookingStatus bookingStatus,
                                                                                    @RequestParam(name = "date", required = false) LocalDate bookingDate){
        List<BookingHistoryResponse> bookings = bookingService.getFilteredBookings(bookingStatus, bookingDate);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        !bookings.isEmpty() ? "Bookings found." : "Bookings not found",
                        bookings,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/bookings/count")
    public ResponseEntity<ApiResponse<Long>> getBookingsCount(){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Booking count retrieved successfully.",
                        bookingService.getBookingsCount(),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/me/bookings")
    public ResponseEntity<ApiResponse<List<BookingHistoryResponse>>> getMyBookings() {

        List<BookingHistoryResponse> bookingHistory =
                bookingService.getMyBookings();

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        bookingHistory.isEmpty()
                                ? "Bookings not found."
                                : "Bookings found.",
                        bookingHistory,
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
}
