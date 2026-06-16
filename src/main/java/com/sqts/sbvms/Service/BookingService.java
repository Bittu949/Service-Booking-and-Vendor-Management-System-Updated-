package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Entity.*;
import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Exception.*;
import com.sqts.sbvms.Repository.*;
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
                request.getUserId() == null ||
                request.getServiceId() == null ||
                request.getBookingDate() == null ||
                request.getTimeSlot() == null)
            throw new InvalidInputException("Please provide all the details.");
        Booking booking = new Booking();
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found."));
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(request.getServiceId()).orElseThrow(() -> new ServiceNotFoundException("Service not found."));
        booking.setUser(user);
        booking.setBookingDate(request.getBookingDate());
        booking.setTimeSlot(request.getTimeSlot());
        booking.setStatus(BookingStatus.PENDING);
        booking.setServiceCategory(serviceCategory);
        bookingRepository.save(booking);

        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setBookingDate(booking.getBookingDate());
        response.setStatus(booking.getStatus());
        response.setTimeSlot(booking.getTimeSlot());
        response.setServiceName(serviceCategory.getServiceName());
        response.setCustomerName(user.getName());
        return response;
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
            pendingBookings.add(response);
        }
        return pendingBookings;
    }
    public List<AvailableVendorResponse> getAvailableVendorsForBooking(Long bookingId){
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
        if(booking.getVendorService() != null) {
            response.setVendorId(booking.getVendorService().getVendor().getId());
            response.setVendorName(booking.getVendorService().getVendor().getUser().getName());
        }

        return response;
    }
}
