package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Tag(
        name = "Booking Management",
        description = "APIs for creating, assigning and managing service bookings."
)
@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @Operation(
            summary = "Create Booking",
            description = "Creates a new booking request for the authenticated customer using the selected service, date, time slot and booking address."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Booking created successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid booking details."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Requested service not found.")
    })
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
    @Operation(
            summary = "Get Customer Booking",
            description = "Retrieves detailed information about a specific booking belonging to the authenticated customer."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "You are not authorized to view this booking."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking not found.")
    })
    @GetMapping("/me/bookings/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDetailsResponse>> getMyBookingById(
            @Parameter(
                    description = "Booking ID",
                    example = "101",
                    required = true
            )
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
    @Operation(
            summary = "Get Vendor Bookings",
            description = "Retrieves all bookings assigned to the authenticated vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor bookings retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @GetMapping("/me/vendor/bookings")
    public ResponseEntity<ApiResponse<List<VendorBookingHistoryResponse>>> getMyVendorBookings() {

        List<VendorBookingHistoryResponse> bookings =
                bookingService.getMyVendorBookings();

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        bookings.isEmpty()
                                ? "Bookings not found."
                                : "Bookings found.",
                        bookings,
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @Operation(
            summary = "Update My Booking Status",
            description = "Allows the authenticated vendor to accept, reject, cancel or complete an assigned booking."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Booking status updated successfully."
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid booking state or requested status."
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to update this booking."
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Booking not found."
            )
    })
    @PatchMapping("/me/bookings/{bookingId}/status")
    public ResponseEntity<ApiResponse<UpdateBookingStatusResponse>>
    updateMyBookingStatus(

            @Parameter(
                    description = "Booking ID",
                    example = "101",
                    required = true
            )
            @PathVariable Long bookingId,

            @RequestBody UpdateBookingStatusRequest request){

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Booking status updated successfully.",
                        bookingService.updateMyBookingStatus(
                                bookingId,
                                request),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @Operation(
            summary = "Get Pending Bookings",
            description = "Retrieves all booking requests that are currently awaiting vendor assignment by an administrator."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pending bookings retrieved successfully.")
    })
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
    @Operation(
            summary = "Get Available Vendors",
            description = "Retrieves all active vendors available for assignment to a pending booking, with optional nearby filtering based on the customer's pincode."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Available vendors retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Booking is not pending."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking not found.")
    })
    @GetMapping("/bookings/{bookingId}/available-vendors")
    public ResponseEntity<ApiResponse<List<AvailableVendorResponse>>> getAvailableVendorsForBooking(
            @Parameter(
                    description = "Booking ID",
                    example = "101",
                    required = true
            )
            @PathVariable(name = "bookingId")
            Long bookingId,
            @Parameter(
                    description = "Return only vendors whose pincode matches the booking address.",
                    example = "true"
            )
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
    @Operation(
            summary = "Assign Vendor To Booking",
            description = "Assigns an available vendor to a pending booking, calculates the final price and changes the booking status to CONFIRMED. The assigned vendor can then accept or reject the booking."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Vendor assigned successfully."
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Vendor unavailable or booking already assigned."
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Booking or vendor service not found."
            )
    })
    @PatchMapping("/bookings/{id}/assign-vendor")
    public ResponseEntity<ApiResponse<AssignVendorResponse>> assignVendorToBooking(
            @RequestBody AssignVendorRequest request,
            @PathVariable(name = "id") Long bookingId) {

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor assigned successfully.",
                        bookingService.assignVendorToBooking(request, bookingId),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @Operation(
            summary = "Get Booking Details",
            description = "Retrieves complete details of a booking, including assigned vendor information, final price and current booking status."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking not found.")
    })
    @GetMapping("/bookings/{id}")
    public ResponseEntity<ApiResponse<BookingDetailsResponse>> getBookingById(
            @Parameter(
                    description = "Booking ID",
                    example = "101",
                    required = true
            )
            @PathVariable(name = "id") Long bookingId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Booking found.",
                        bookingService.getBookingById(bookingId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Get Customer Booking History",
            description = "Retrieves the complete booking history of a specific customer."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer booking history retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    @GetMapping("/users/{id}/bookings")
    public ResponseEntity<ApiResponse<List<BookingHistoryResponse>>> getCustomerBookingHistory(
            @Parameter(
                    description = "Customer ID",
                    example = "5",
                    required = true
            )
            @PathVariable(name = "id") Long customerId){
        List<BookingHistoryResponse> bookingHistory = bookingService.getCustomerBookingHistory(customerId);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        !bookingHistory.isEmpty() ? "Bookings found." : "Bookings not found.",
                        bookingHistory,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Cancel Booking",
            description = "Allows the administrator to cancel a pending or confirmed booking before the vendor starts the service."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Booking cancelled successfully."
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Only pending or confirmed bookings can be cancelled."
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Booking not found."
            )
    })
    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<ApiResponse<UpdateBookingStatusResponse>> updateBookingStatus(
            @RequestBody UpdateBookingStatusRequest request,
            @Parameter(
                    description = "Booking ID",
                    example = "101",
                    required = true
            )
            @PathVariable(name = "id") Long bookingId) {

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Booking status updated successfully.",
                        bookingService.updateBookingStatus(bookingId, request),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @Operation(
            summary = "Get Vendor Booking History",
            description = "Retrieves the complete booking history of a specific vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor booking history retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @GetMapping("/vendors/{id}/bookings")
    public ResponseEntity<ApiResponse<List<VendorBookingHistoryResponse>>> getVendorBookingHistory(
            @Parameter(
                    description = "Vendor ID",
                    example = "10",
                    required = true
            )
            @PathVariable(name = "id") Long vendorId){
        List<VendorBookingHistoryResponse> vendorBookings = bookingService.getVendorBookingHistory(vendorId);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        !vendorBookings.isEmpty() ? "Booking found." : "Bookings not found",
                        vendorBookings,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Filter Bookings",
            description = "Retrieves bookings filtered by booking status and/or booking date. Supported statuses include PENDING, CONFIRMED, IN_PROGRESS, COMPLETED and CANCELLED."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Bookings retrieved successfully."
            )
    })
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<AdminBookingHistoryResponse>>> getFilteredBookings(

            @Parameter(
                    description = "Booking status (PENDING, CONFIRMED, IN_PROGRESS, COMPLETED or CANCELLED)",
                    example = "IN_PROGRESS"
            )
            @RequestParam(name = "status", required = false)
            BookingStatus bookingStatus,

            @Parameter(
                    description = "Booking date",
                    example = "2026-06-30"
            )
            @RequestParam(name = "date", required = false)
            LocalDate bookingDate) {

        List<AdminBookingHistoryResponse> bookings =
                bookingService.getFilteredBookings(
                        bookingStatus,
                        bookingDate);

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        !bookings.isEmpty()
                                ? "Bookings found."
                                : "Bookings not found.",
                        bookings,
                        LocalDateTime.now()
                ),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Get Total Bookings",
            description = "Returns the total number of bookings in the system."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking count retrieved successfully.")
    })
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
    @Operation(
            summary = "Get My Booking History",
            description = "Retrieves the complete booking history of the authenticated customer."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer booking history retrieved successfully.")
    })
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
    @Operation(
            summary = "Cancel My Booking",
            description = "Allows the authenticated customer to cancel one of their pending or confirmed bookings before the assigned vendor starts the service."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking cancelled successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Only pending or confirmed bookings can be cancelled."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "You are not authorized to cancel this booking."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking not found.")
    })
    @PatchMapping("/me/bookings/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<UpdateBookingStatusResponse>> cancelMyBooking(
            @Parameter(
                    description = "Booking ID",
                    example = "101",
                    required = true
            )
            @PathVariable Long bookingId) {

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Booking cancelled successfully.",
                        bookingService.cancelMyBooking(bookingId),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
}
