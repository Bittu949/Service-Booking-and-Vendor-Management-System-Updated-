package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.BookingRequest;
import com.sqts.sbvms.Dto.BookingResponse;
import com.sqts.sbvms.Entity.Booking;
import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Entity.User;
import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Exception.BookingsNotFoundException;
import com.sqts.sbvms.Exception.InvalidTimeSlotException;
import com.sqts.sbvms.Exception.ServiceNotFoundException;
import com.sqts.sbvms.Exception.UserNotFoundException;
import com.sqts.sbvms.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
        LocalTime newStartTime = request.getTimeSlot().getStartTime();
        LocalTime newEndTime = request.getTimeSlot().getEndTime();

        List<Booking> bookings = bookingRepository.findByBookingDate(request.getBookingDate());

        if(!bookings.isEmpty()) {
            bookings = bookings.stream().filter(b -> newStartTime.isBefore(b.getTimeSlot().getEndTime()) && newEndTime.isAfter(b.getTimeSlot().getStartTime())).toList();
            if(!bookings.isEmpty())
                throw new InvalidTimeSlotException("Please choose different timeslot.");
        }
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
}
