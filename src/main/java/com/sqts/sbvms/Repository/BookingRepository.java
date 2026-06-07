package com.sqts.sbvms.Repository;

import com.sqts.sbvms.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookingDate(LocalDate bookingDate);
}
