package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Entity.*;
import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Exception.*;
import com.sqts.sbvms.Repository.*;
import com.sqts.sbvms.Security.CustomUserDetails;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
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
    public BookingResponse createBooking(BookingRequest request){
        if(request == null ||
                request.getServiceId() == null ||
                request.getBookingDate() == null ||
                request.getTimeSlot() == null ||
                request.getBookingAddress() == null)
            throw new InvalidInputException("Please provide all the details.");
        Booking booking = new Booking();
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(request.getServiceId()).orElseThrow(() -> new ServiceNotFoundException("Service not found."));
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
    public BookingDetailsResponse getMyBookingById(Long bookingId){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found."));

        if(!booking.getUser().getId().equals(userDetails.getId()))
            throw new UnauthorizedAccessException(
                    "You are not authorized to view this booking.");

        BookingDetailsResponse response = new BookingDetailsResponse();

        response.setBookingId(booking.getId());
        response.setBookingDate(booking.getBookingDate());
        response.setBookingStatus(booking.getStatus());
        response.setStartTime(booking.getTimeSlot().getStartTime());
        response.setEndTime(booking.getTimeSlot().getEndTime());
        response.setServiceName(booking.getServiceCategory().getServiceName());
        response.setCustomerName(booking.getUser().getName());
        response.setBookingAddress(booking.getBookingAddress());

        if(booking.getVendorService() != null){

            response.setVendorId(booking.getVendorService().getVendor().getId());
            response.setVendorName(booking.getVendorService().getVendor().getUser().getName());
            response.setVendorAddress(booking.getVendorService().getVendor().getVendorAddress());
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
    public List<PendingBookingResponse> getPendingBookings(){
        List<PendingBookingResponse> pendingBookings = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.PENDING);

        for(Booking booking : bookings){
            PendingBookingResponse response = new PendingBookingResponse();
            response.setBookingId(booking.getId());
            response.setBookingDate(booking.getBookingDate());
            response.setServiceName(booking.getServiceCategory().getServiceName());
            response.setCustomerName(booking.getUser().getName());
            response.setStartTime(booking.getTimeSlot().getStartTime());
            response.setEndTime(booking.getTimeSlot().getEndTime());
            response.setStatus(booking.getStatus());
            response.setBookingAddress(booking.getBookingAddress());
            pendingBookings.add(response);
        }
        return pendingBookings;
    }
    public List<AvailableVendorResponse> getAvailableVendorsForBooking(Long bookingId, boolean nearby){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Booking not found."));

        if(booking.getStatus() != BookingStatus.PENDING)
            throw new InvalidBookingStateException("Available vendors can only be fetched for pending bookings.");

        ServiceCategory serviceCategory = booking.getServiceCategory();
        LocalDate bookingDate = booking.getBookingDate();
        LocalTime bookingStart = booking.getTimeSlot().getStartTime();
        LocalTime bookingEnd = booking.getTimeSlot().getEndTime();
        List<AvailableVendorResponse> availableVendors = new ArrayList<>();

        //Fetching all the vendor service for particular service category
        List<VendorService> vendorServices =
                vendorServiceRepository.findByServiceCategoryId(serviceCategory.getId());
        //Filtering only those vendor services whose vendors are active
        vendorServices = vendorServices.stream().filter(v -> v.getVendor().getStatus() == VendorStatus.ACTIVE).toList();

        //Check if nearby vendors only needed
        if(nearby)
            vendorServices = vendorServices.stream().filter(v -> v.getVendor().getVendorAddress().getPincode().equals(booking.getBookingAddress().getPincode())).toList();

        for(VendorService vendorService : vendorServices){
            List<Booking> vendorBookings =
                    bookingRepository.findByVendorServiceVendorId(vendorService.getVendor().getId());

            boolean hasOverlap = vendorBookings.stream()
                    .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                    .filter(b -> b.getBookingDate().equals(bookingDate))
                    .anyMatch(b ->
                            bookingStart.isBefore(b.getTimeSlot().getEndTime())
                                    &&
                                    bookingEnd.isAfter(b.getTimeSlot().getStartTime())
                    );
            if(!hasOverlap){
                Vendor vendor = vendorService.getVendor();
                AvailableVendorResponse response = new AvailableVendorResponse();
                response.setVendorId(vendor.getId());
                response.setVendorName(vendor.getUser().getName());
                response.setVendorEmail(vendor.getUser().getEmail());
                response.setStatus(vendor.getStatus());
                response.setPrice(vendorService.getPrice());
                response.setVendorAddress(vendor.getVendorAddress());

                availableVendors.add(response);
            }
        }
        return availableVendors;
    }
    public AssignVendorResponse assignVendorToBooking(AssignVendorRequest request,
                                                      Long bookingId){
        VendorService vendorService = vendorServiceRepository.findById(request.getVendorServiceId())
                .orElseThrow(() -> new ServiceAssignmentNotFoundException("No vendor service found."));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found."));

        if(vendorService.getVendor().getStatus() != VendorStatus.ACTIVE)
            throw new InvalidVendorStatusException("Please select active vendor.");

        if(booking.getStatus() != BookingStatus.PENDING)
            throw new InvalidBookingStateException("This booking may have been completed or already assigned to a vendor.");

        LocalDate bookingDate = booking.getBookingDate();
        LocalTime bookingStart = booking.getTimeSlot().getStartTime();
        LocalTime bookingEnd = booking.getTimeSlot().getEndTime();

        //Get all bookings of this vendor
        List<Booking> vendorBookings =
                bookingRepository.findByVendorServiceVendorId(vendorService.getVendor().getId());

        boolean hasOverlap = vendorBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .filter(b -> b.getBookingDate().equals(bookingDate))
                .anyMatch(b ->
                        bookingStart.isBefore(b.getTimeSlot().getEndTime())
                                &&
                                bookingEnd.isAfter(b.getTimeSlot().getStartTime())
                );
        if(hasOverlap){
           throw new InvalidTimeSlotException("Vendor is not available for this time slot.");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setVendorService(vendorService);
        bookingRepository.save(booking);

        AssignVendorResponse response = new AssignVendorResponse();
        response.setBookingId(booking.getId());
        response.setBookingStatus(booking.getStatus());
        response.setBookingDate(booking.getBookingDate());
        response.setServiceName(booking.getServiceCategory().getServiceName());
        response.setCustomerName(booking.getUser().getName());
        response.setVendorName(booking.getVendorService().getVendor().getUser().getName());
        response.setStartTime(booking.getTimeSlot().getStartTime());
        response.setEndTime(booking.getTimeSlot().getEndTime());
        response.setBookingAddress(booking.getBookingAddress());
        response.setVendorAddress(booking.getVendorService().getVendor().getVendorAddress());

        return response;
    }
    public BookingDetailsResponse getBookingById(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found."));
        BookingDetailsResponse response = new BookingDetailsResponse();
        response.setBookingId(booking.getId());
        response.setBookingDate(booking.getBookingDate());
        response.setBookingStatus(booking.getStatus());
        response.setStartTime(booking.getTimeSlot().getStartTime());
        response.setEndTime(booking.getTimeSlot().getEndTime());
        response.setServiceName(booking.getServiceCategory().getServiceName());
        response.setCustomerName(booking.getUser().getName());
        response.setBookingAddress(booking.getBookingAddress());

        if(booking.getVendorService() != null) {
            response.setVendorId(booking.getVendorService().getVendor().getId());
            response.setVendorName(booking.getVendorService().getVendor().getUser().getName());
            response.setVendorAddress(booking.getVendorService().getVendor().getVendorAddress());
        }

        return response;
    }
    public List<BookingHistoryResponse> getCustomerBookingHistory(Long customerId){
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new UserNotFoundException("Customer not found."));

        List<BookingHistoryResponse> responses = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findByUserId(customer.getId());

        for(Booking booking : bookings){
            BookingHistoryResponse bookingHistoryResponse = new BookingHistoryResponse();
            bookingHistoryResponse.setBookingId(booking.getId());
            bookingHistoryResponse.setBookingDate(booking.getBookingDate());
            bookingHistoryResponse.setBookingStatus(booking.getStatus());
            bookingHistoryResponse.setStartTime(booking.getTimeSlot().getStartTime());
            bookingHistoryResponse.setEndTime(booking.getTimeSlot().getEndTime());
            bookingHistoryResponse.setServiceName(booking.getServiceCategory().getServiceName());
            bookingHistoryResponse.setBookingAddress(booking.getBookingAddress());

            VendorService vendorService = booking.getVendorService();
            if(vendorService != null) {
                Vendor vendor = vendorService.getVendor();
                bookingHistoryResponse.setVendorId(vendor.getId());
                bookingHistoryResponse.setVendorName(vendor.getUser().getName());
                bookingHistoryResponse.setVendorAddress(vendor.getVendorAddress());
            }
            responses.add(bookingHistoryResponse);
        }
        return responses;
    }
    public UpdateBookingStatusResponse updateBookingStatus(Long bookingId, UpdateBookingStatusRequest request){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found."));

        if(booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED)
            throw new InvalidBookingStateException("Booking status is already "+booking.getStatus());

        if(booking.getStatus() == BookingStatus.PENDING && request.getStatus() != BookingStatus.CANCELLED)
            throw new InvalidBookingStateException("Cannot change status "+request.getStatus()+" for PENDING bookings.");

        if (booking.getStatus() == BookingStatus.CONFIRMED &&
                request.getStatus() != BookingStatus.COMPLETED &&
                request.getStatus() != BookingStatus.CANCELLED) {

            throw new InvalidBookingStateException(
                    "Cannot change status " + request.getStatus() + " for CONFIRMED bookings.");
        }

        UpdateBookingStatusResponse response = new UpdateBookingStatusResponse();
        response.setBookingId(booking.getId());
        response.setPreviousStatus(booking.getStatus());

        booking.setStatus(request.getStatus());
        bookingRepository.save(booking);

        response.setCurrentStatus(booking.getStatus());
        return response;
    }
    public List<VendorBookingHistoryResponse> getVendorBookingHistory(Long vendorId){
        if (!vendorRepository.existsById(vendorId)) {
            throw new VendorNotFoundException("Vendor not found.");
        }
        List<Booking> bookings = bookingRepository.findByVendorServiceVendorId(vendorId);
        List<VendorBookingHistoryResponse> responses = new ArrayList<>();

        for (Booking booking : bookings){
            VendorBookingHistoryResponse bookingHistoryResponse = new VendorBookingHistoryResponse();
            bookingHistoryResponse.setBookingId(booking.getId());
            bookingHistoryResponse.setCustomerName(booking.getUser().getName());
            bookingHistoryResponse.setServiceName(booking.getServiceCategory().getServiceName());
            bookingHistoryResponse.setBookingStatus(booking.getStatus());
            bookingHistoryResponse.setDate(booking.getBookingDate());
            bookingHistoryResponse.setTimeSlot(booking.getTimeSlot());
            bookingHistoryResponse.setBookingAddress(booking.getBookingAddress());

            responses.add(bookingHistoryResponse);
        }
        return responses;
    }
    public List<BookingHistoryResponse> getFilteredBookings(BookingStatus bookingStatus, LocalDate bookingDate){
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingHistoryResponse> allBookings = new ArrayList<>();

        //Filter bookings on booking status
        if(bookingStatus != null)
            bookings = bookings.stream().filter(b -> b.getStatus() == bookingStatus).toList();

        //Filter bookings on booking date
        if(bookingDate != null)
            bookings = bookings.stream().filter(b -> b.getBookingDate().equals(bookingDate)).toList();

        for(Booking booking : bookings){
            BookingHistoryResponse response = new BookingHistoryResponse();
            VendorService vendorService = booking.getVendorService();
            if(vendorService != null) {
                Vendor vendor = vendorService.getVendor();
                response.setVendorId(vendor.getId());
                response.setVendorName(vendor.getUser().getName());
                response.setVendorAddress(vendor.getVendorAddress());
            }
            response.setBookingId(booking.getId());
            response.setBookingStatus(booking.getStatus());
            response.setBookingDate(booking.getBookingDate());
            response.setServiceName(booking.getServiceCategory().getServiceName());
            response.setStartTime(booking.getTimeSlot().getStartTime());
            response.setEndTime(booking.getTimeSlot().getEndTime());
            response.setBookingAddress(booking.getBookingAddress());

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
}
