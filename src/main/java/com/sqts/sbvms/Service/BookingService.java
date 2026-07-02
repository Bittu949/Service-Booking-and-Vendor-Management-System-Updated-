package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Entity.*;
import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Exception.*;
import com.sqts.sbvms.Repository.*;
import com.sqts.sbvms.Security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final VendorServiceRepository vendorServiceRepository;
    private final VendorRepository vendorRepository;
    public BookingService(BookingRepository bookingRepository,
                          UserRepository userRepository,
                          ServiceCategoryRepository serviceCategoryRepository,
                          VendorServiceRepository vendorServiceRepository,
                          VendorRepository vendorRepository){
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.vendorServiceRepository = vendorServiceRepository;
        this.vendorRepository = vendorRepository;
    }
    public BookingResponse createBooking(BookingRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        ServiceCategory serviceCategory =
                serviceCategoryRepository.findById(request.getServiceId())
                        .orElseThrow(() ->
                                new ServiceNotFoundException("Service not found."));

        if (request.getBookingDate().isBefore(LocalDate.now()))
            throw new InvalidBookingDateException(
                    "Booking date cannot be in the past.");

        if (!request.getTimeSlot().getStartTime()
                .isBefore(request.getTimeSlot().getEndTime()))
            throw new InvalidTimeSlotException(
                    "Start time must be before end time.");

        if (request.getBookingDate().equals(LocalDate.now())
                &&
                request.getTimeSlot().getStartTime().isBefore(LocalTime.now()))
            throw new InvalidTimeSlotException(
                    "Booking time cannot be in the past.");

        if (bookingRepository.existsByUser_IdAndServiceCategory_IdAndBookingDateAndTimeSlotAndStatusIn(
                user.getId(),
                serviceCategory.getId(),
                request.getBookingDate(),
                request.getTimeSlot(),
                List.of(
                        BookingStatus.PENDING,
                        BookingStatus.CONFIRMED
                )))
            throw new DuplicateBookingException(
                    "You already have an active booking for this service at the selected date and time.");

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setBookingDate(request.getBookingDate());
        booking.setTimeSlot(request.getTimeSlot());
        booking.setStatus(BookingStatus.PENDING);
        booking.setServiceCategory(serviceCategory);
        booking.setBookingAddress(request.getBookingAddress());

        bookingRepository.save(booking);

        BookingResponse response = new BookingResponse();

        response.setBookingId(booking.getId());
        response.setBookingDate(booking.getBookingDate());
        response.setStatus(booking.getStatus());
        response.setTimeSlot(booking.getTimeSlot());
        response.setServiceName(serviceCategory.getServiceName());
        response.setCustomerName(user.getName());
        response.setBookingAddress(booking.getBookingAddress());

        return response;
    }
    public BookingDetailsResponse getMyBookingById(Long bookingId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found."));

        if (!booking.getUser().getId().equals(userDetails.getId()))
            throw new UnauthorizedAccessException(
                    "You are not authorized to view this booking.");

        BookingDetailsResponse response =
                new BookingDetailsResponse();

        response.setBookingId(booking.getId());
        response.setBookingDate(booking.getBookingDate());
        response.setBookingStatus(booking.getStatus());
        response.setStartTime(booking.getTimeSlot().getStartTime());
        response.setEndTime(booking.getTimeSlot().getEndTime());
        response.setServiceName(booking.getServiceCategory().getServiceName());
        response.setCustomerName(booking.getUser().getName());
        response.setBookingAddress(booking.getBookingAddress());

        if (booking.getVendorService() != null) {

            response.setVendorId(
                    booking.getVendorService().getVendor().getId());

            response.setVendorName(
                    booking.getVendorService().getVendor().getUser().getName());

            response.setVendorAddress(
                    booking.getVendorService().getVendor().getVendorAddress());

            response.setFinalPrice(
                    booking.getFinalPrice());

            Duration duration = booking.getEstimatedDuration();

            if (duration != null) {
                response.setEstimatedDuration(
                        String.format(
                                "%02d:%02d",
                                duration.toHours(),
                                duration.toMinutesPart()
                        )
                );
            }

            response.setAssignedAt(
                    booking.getAssignedAt());

            response.setCompletedAt(
                    booking.getCompletedAt());
        }

        return response;
    }
    public List<VendorBookingHistoryResponse> getMyVendorBookings() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Vendor vendor = vendorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        return getVendorBookingHistory(vendor.getId());
    }
    public UpdateBookingStatusResponse updateMyBookingStatus(
            Long bookingId,
            UpdateBookingStatusRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Vendor vendor = vendorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found."));

        if (booking.getVendorService() == null)
            throw new InvalidBookingStateException(
                    "No vendor has been assigned to this booking.");

        if (!booking.getVendorService().getVendor().getId().equals(vendor.getId()))
            throw new UnauthorizedAccessException(
                    "You are not authorized to update this booking.");

        BookingStatus currentStatus = booking.getStatus();
        BookingStatus requestedStatus = request.getStatus();

        switch (currentStatus) {

            case CONFIRMED -> {

                if (requestedStatus != BookingStatus.IN_PROGRESS &&
                        requestedStatus != BookingStatus.CANCELLED)
                    throw new InvalidBookingStateException(
                            "A confirmed booking can only be updated to IN_PROGRESS or CANCELLED.");
            }

            case IN_PROGRESS -> {

                if (requestedStatus != BookingStatus.COMPLETED &&
                        requestedStatus != BookingStatus.CANCELLED)
                    throw new InvalidBookingStateException(
                            "An in-progress booking can only be updated to COMPLETED or CANCELLED.");
            }

            default ->
                    throw new InvalidBookingStateException(
                            "This booking cannot be updated.");
        }

        UpdateBookingStatusResponse response =
                new UpdateBookingStatusResponse();

        response.setBookingId(booking.getId());
        response.setPreviousStatus(currentStatus);

        booking.setStatus(requestedStatus);

        if (requestedStatus == BookingStatus.COMPLETED) {
            booking.setCompletedAt(LocalDateTime.now());
        }

        bookingRepository.save(booking);

        response.setCurrentStatus(booking.getStatus());

        return response;
    }
    public List<PendingBookingResponse> getPendingBookings() {

        List<Booking> bookings =
                bookingRepository.findByStatus(BookingStatus.PENDING);

        List<PendingBookingResponse> pendingBookings =
                new ArrayList<>();

        for (Booking booking : bookings) {

            PendingBookingResponse response =
                    new PendingBookingResponse();

            response.setBookingId(booking.getId());
            response.setBookingDate(booking.getBookingDate());
            response.setServiceName(
                    booking.getServiceCategory().getServiceName());
            response.setCustomerName(
                    booking.getUser().getName());
            response.setStartTime(
                    booking.getTimeSlot().getStartTime());
            response.setEndTime(
                    booking.getTimeSlot().getEndTime());
            response.setStatus(
                    booking.getStatus());
            response.setBookingAddress(
                    booking.getBookingAddress());

            pendingBookings.add(response);
        }

        return pendingBookings;
    }
    public List<AvailableVendorResponse> getAvailableVendorsForBooking(
            Long bookingId,
            boolean nearby) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found."));

        if (booking.getStatus() != BookingStatus.PENDING)
            throw new InvalidBookingStateException(
                    "Available vendors can only be fetched for pending bookings.");

        ServiceCategory serviceCategory = booking.getServiceCategory();

        LocalDate bookingDate = booking.getBookingDate();

        LocalTime bookingStart =
                booking.getTimeSlot().getStartTime();

        LocalTime bookingEnd =
                booking.getTimeSlot().getEndTime();

        List<VendorService> vendorServices =
                vendorServiceRepository.findByServiceCategoryId(
                        serviceCategory.getId());

        // Only ACTIVE vendors
        vendorServices = vendorServices.stream()
                .filter(v ->
                        v.getVendor().getStatus() == VendorStatus.ACTIVE)
                .toList();

        // Nearby vendors only
        if (nearby) {

            vendorServices = vendorServices.stream()
                    .filter(v ->
                            v.getVendor()
                                    .getVendorAddress()
                                    .getPincode()
                                    .equals(
                                            booking.getBookingAddress()
                                                    .getPincode()
                                    )
                    )
                    .toList();
        }

        List<AvailableVendorResponse> availableVendors =
                new ArrayList<>();

        for (VendorService vendorService : vendorServices) {

            List<Booking> vendorBookings =
                    bookingRepository.findByVendorServiceVendorId(
                            vendorService.getVendor().getId());

            boolean hasOverlap = vendorBookings.stream()

                    .filter(b ->
                            b.getStatus() == BookingStatus.CONFIRMED
                                    ||
                                    b.getStatus() == BookingStatus.IN_PROGRESS)

                    .filter(b ->
                            b.getBookingDate().equals(bookingDate))

                    .anyMatch(b ->
                            bookingStart.isBefore(
                                    b.getTimeSlot().getEndTime())
                                    &&
                                    bookingEnd.isAfter(
                                            b.getTimeSlot().getStartTime()));

            if (!hasOverlap) {

                Vendor vendor = vendorService.getVendor();

                AvailableVendorResponse response =
                        new AvailableVendorResponse();

                response.setVendorServiceId(vendorService.getId());

                response.setVendorId(vendor.getId());

                response.setVendorName(
                        vendor.getUser().getName());

                response.setVendorEmail(
                        vendor.getUser().getEmail());

                response.setStatus(
                        vendor.getStatus());

                response.setPrice(
                        vendorService.getPrice());

                Duration duration =
                        vendorService.getDuration();

                response.setEstimatedDuration(
                        String.format(
                                "%02d:%02d",
                                duration.toHours(),
                                duration.toMinutesPart()
                        )
                );

                response.setVendorAddress(
                        vendor.getVendorAddress());

                availableVendors.add(response);
            }
        }

        return availableVendors;
    }
    public AssignVendorResponse assignVendorToBooking(
            AssignVendorRequest request,
            Long bookingId) {

        VendorService vendorService = vendorServiceRepository
                .findById(request.getVendorServiceId())
                .orElseThrow(() ->
                        new ServiceAssignmentNotFoundException(
                                "No vendor service found."));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found."));

        if (vendorService.getVendor().getStatus() != VendorStatus.ACTIVE)
            throw new InvalidVendorStatusException(
                    "Please select an active vendor.");

        if (booking.getStatus() != BookingStatus.PENDING)
            throw new InvalidBookingStateException(
                    "Only pending bookings can be assigned to a vendor.");

        LocalDate bookingDate = booking.getBookingDate();
        LocalTime bookingStart = booking.getTimeSlot().getStartTime();
        LocalTime bookingEnd = booking.getTimeSlot().getEndTime();

        List<Booking> vendorBookings =
                bookingRepository.findByVendorServiceVendorId(
                        vendorService.getVendor().getId());

        boolean hasOverlap = vendorBookings.stream()
                .filter(b ->
                        b.getStatus() == BookingStatus.CONFIRMED
                                || b.getStatus() == BookingStatus.IN_PROGRESS)
                .filter(b ->
                        b.getBookingDate().equals(bookingDate))
                .anyMatch(b ->
                        bookingStart.isBefore(
                                b.getTimeSlot().getEndTime())
                                &&
                                bookingEnd.isAfter(
                                        b.getTimeSlot().getStartTime()));

        if (hasOverlap)
            throw new InvalidTimeSlotException(
                    "Vendor is not available for this time slot.");

        Duration bookingDuration =
                Duration.between(bookingStart, bookingEnd);

        long bookingMinutes = bookingDuration.toMinutes();

        long vendorMinutes = vendorService.getDuration().toMinutes();

        long finalPrice =
                (vendorService.getPrice() * bookingMinutes)
                        / vendorMinutes;

        booking.setVendorService(vendorService);
        booking.setFinalPrice(finalPrice);
        booking.setEstimatedDuration(vendorService.getDuration());
        booking.setAssignedAt(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);

        AssignVendorResponse response =
                new AssignVendorResponse();

        response.setBookingId(booking.getId());
        response.setBookingStatus(booking.getStatus());
        response.setBookingDate(booking.getBookingDate());
        response.setServiceName(
                booking.getServiceCategory().getServiceName());
        response.setCustomerName(
                booking.getUser().getName());
        response.setVendorName(
                vendorService.getVendor().getUser().getName());
        response.setStartTime(
                booking.getTimeSlot().getStartTime());
        response.setEndTime(
                booking.getTimeSlot().getEndTime());
        response.setBookingAddress(
                booking.getBookingAddress());
        response.setVendorAddress(
                vendorService.getVendor().getVendorAddress());
        response.setFinalPrice(
                booking.getFinalPrice());

        Duration duration = booking.getEstimatedDuration();

        response.setEstimatedDuration(
                String.format(
                        "%02d:%02d",
                        duration.toHours(),
                        duration.toMinutesPart()
                )
        );

        return response;
    }
    public BookingDetailsResponse getBookingById(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found."));

        BookingDetailsResponse response =
                new BookingDetailsResponse();

        response.setBookingId(booking.getId());
        response.setBookingDate(booking.getBookingDate());
        response.setBookingStatus(booking.getStatus());
        response.setStartTime(booking.getTimeSlot().getStartTime());
        response.setEndTime(booking.getTimeSlot().getEndTime());
        response.setServiceName(booking.getServiceCategory().getServiceName());
        response.setCustomerName(booking.getUser().getName());
        response.setBookingAddress(booking.getBookingAddress());

        if (booking.getVendorService() != null) {

            response.setVendorId(
                    booking.getVendorService().getVendor().getId());

            response.setVendorName(
                    booking.getVendorService().getVendor().getUser().getName());

            response.setVendorAddress(
                    booking.getVendorService().getVendor().getVendorAddress());

            response.setFinalPrice(
                    booking.getFinalPrice());

            if (booking.getEstimatedDuration() != null) {

                response.setEstimatedDuration(
                        String.format(
                                "%02d:%02d",
                                booking.getEstimatedDuration().toHours(),
                                booking.getEstimatedDuration().toMinutesPart()
                        )
                );
            }

            response.setAssignedAt(
                    booking.getAssignedAt());

            response.setCompletedAt(
                    booking.getCompletedAt());
        }

        return response;
    }
    public List<BookingHistoryResponse> getCustomerBookingHistory(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new UserNotFoundException("Customer not found."));

        List<Booking> bookings =
                bookingRepository.findByUserId(customer.getId());

        List<BookingHistoryResponse> responses =
                new ArrayList<>();

        for (Booking booking : bookings) {

            BookingHistoryResponse response =
                    new BookingHistoryResponse();

            response.setBookingId(booking.getId());
            response.setBookingDate(booking.getBookingDate());
            response.setBookingStatus(booking.getStatus());
            response.setStartTime(booking.getTimeSlot().getStartTime());
            response.setEndTime(booking.getTimeSlot().getEndTime());
            response.setServiceName(
                    booking.getServiceCategory().getServiceName());
            response.setBookingAddress(
                    booking.getBookingAddress());

            VendorService vendorService = booking.getVendorService();

            if (vendorService != null) {

                Vendor vendor = vendorService.getVendor();

                response.setVendorId(vendor.getId());
                response.setVendorName(vendor.getUser().getName());
                response.setVendorAddress(vendor.getVendorAddress());
                response.setFinalPrice(booking.getFinalPrice());

                if (booking.getEstimatedDuration() != null) {

                    response.setEstimatedDuration(
                            String.format(
                                    "%02d:%02d",
                                    booking.getEstimatedDuration().toHours(),
                                    booking.getEstimatedDuration().toMinutesPart()
                            )
                    );
                }

                response.setAssignedAt(booking.getAssignedAt());
                response.setCompletedAt(booking.getCompletedAt());
            }

            responses.add(response);
        }

        return responses;
    }
    public UpdateBookingStatusResponse updateBookingStatus(
            Long bookingId,
            UpdateBookingStatusRequest request) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found."));

        if (booking.getStatus() != BookingStatus.PENDING
                &&
                booking.getStatus() != BookingStatus.CONFIRMED)
            throw new InvalidBookingStateException(
                    "Only pending or confirmed bookings can be cancelled by the administrator.");

        if (request.getStatus() != BookingStatus.CANCELLED)
            throw new InvalidBookingStateException(
                    "Administrator can only change the booking status to CANCELLED.");

        UpdateBookingStatusResponse response =
                new UpdateBookingStatusResponse();

        response.setBookingId(booking.getId());
        response.setPreviousStatus(booking.getStatus());

        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);

        response.setCurrentStatus(booking.getStatus());

        return response;
    }
    public List<VendorBookingHistoryResponse> getVendorBookingHistory(Long vendorId) {

        if (!vendorRepository.existsById(vendorId)) {
            throw new VendorNotFoundException("Vendor not found.");
        }

        List<Booking> bookings =
                bookingRepository.findByVendorServiceVendorId(vendorId);

        List<VendorBookingHistoryResponse> responses =
                new ArrayList<>();

        for (Booking booking : bookings) {

            VendorBookingHistoryResponse response =
                    new VendorBookingHistoryResponse();

            response.setBookingId(booking.getId());
            response.setCustomerName(booking.getUser().getName());
            response.setServiceName(
                    booking.getServiceCategory().getServiceName());
            response.setBookingStatus(booking.getStatus());
            response.setDate(booking.getBookingDate());
            response.setTimeSlot(booking.getTimeSlot());
            response.setBookingAddress(booking.getBookingAddress());
            response.setFinalPrice(booking.getFinalPrice());

            if (booking.getEstimatedDuration() != null) {

                response.setEstimatedDuration(
                        String.format(
                                "%02d:%02d",
                                booking.getEstimatedDuration().toHours(),
                                booking.getEstimatedDuration().toMinutesPart()
                        )
                );
            }

            response.setAssignedAt(booking.getAssignedAt());
            response.setCompletedAt(booking.getCompletedAt());

            responses.add(response);
        }

        return responses;
    }
    public List<BookingHistoryResponse> getFilteredBookings(
            BookingStatus bookingStatus,
            LocalDate bookingDate) {

        List<Booking> bookings = bookingRepository.findAll();

        List<BookingHistoryResponse> allBookings =
                new ArrayList<>();

        // Filter bookings by status
        if (bookingStatus != null)
            bookings = bookings.stream()
                    .filter(b -> b.getStatus() == bookingStatus)
                    .toList();

        // Filter bookings by date
        if (bookingDate != null)
            bookings = bookings.stream()
                    .filter(b -> b.getBookingDate().equals(bookingDate))
                    .toList();

        for (Booking booking : bookings) {

            BookingHistoryResponse response =
                    new BookingHistoryResponse();

            VendorService vendorService = booking.getVendorService();

            if (vendorService != null) {

                Vendor vendor = vendorService.getVendor();

                response.setVendorId(vendor.getId());
                response.setVendorName(vendor.getUser().getName());
                response.setVendorAddress(vendor.getVendorAddress());
                response.setFinalPrice(booking.getFinalPrice());

                if (booking.getEstimatedDuration() != null) {

                    response.setEstimatedDuration(
                            String.format(
                                    "%02d:%02d",
                                    booking.getEstimatedDuration().toHours(),
                                    booking.getEstimatedDuration().toMinutesPart()
                            )
                    );
                }

                response.setAssignedAt(booking.getAssignedAt());
                response.setCompletedAt(booking.getCompletedAt());
            }

            response.setBookingId(booking.getId());
            response.setBookingStatus(booking.getStatus());
            response.setBookingDate(booking.getBookingDate());
            response.setServiceName(
                    booking.getServiceCategory().getServiceName());
            response.setStartTime(
                    booking.getTimeSlot().getStartTime());
            response.setEndTime(
                    booking.getTimeSlot().getEndTime());
            response.setBookingAddress(
                    booking.getBookingAddress());

            allBookings.add(response);
        }

        return allBookings;
    }
    public Long getBookingsCount(){
        return bookingRepository.count();
    }
    public List<BookingHistoryResponse> getMyBookings() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long customerId = userDetails.getId();

        return getCustomerBookingHistory(customerId);
    }
    public UpdateBookingStatusResponse cancelMyBooking(Long bookingId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found."));

        if (!booking.getUser().getId().equals(userDetails.getId()))
            throw new UnauthorizedException(
                    "You can only cancel your own bookings.");

        if (booking.getStatus() != BookingStatus.PENDING
                &&
                booking.getStatus() != BookingStatus.CONFIRMED)
            throw new InvalidOperationException(
                    "Booking can only be cancelled before the vendor starts the service.");

        BookingStatus previousStatus = booking.getStatus();

        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);

        UpdateBookingStatusResponse response =
                new UpdateBookingStatusResponse();

        response.setBookingId(booking.getId());
        response.setPreviousStatus(previousStatus);
        response.setCurrentStatus(booking.getStatus());

        return response;
    }
}
