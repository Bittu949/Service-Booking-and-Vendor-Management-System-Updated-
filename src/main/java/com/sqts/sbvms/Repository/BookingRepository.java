package com.sqts.sbvms.Repository;

import com.sqts.sbvms.Entity.Booking;
import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookingDate(LocalDate bookingDate);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByVendorServiceVendorId(Long vendorId);
    List<Booking> findByUserId(Long userId);
    boolean existsByUser_IdAndServiceCategory_IdAndBookingDateAndTimeSlotAndStatusIn(
            Long userId,
            Long serviceCategoryId,
            LocalDate bookingDate,
            TimeSlot timeSlot,
            List<BookingStatus> statuses
    );

}
