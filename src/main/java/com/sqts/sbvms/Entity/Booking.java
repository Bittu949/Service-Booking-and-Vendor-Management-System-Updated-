package com.sqts.sbvms.Entity;

import com.sqts.sbvms.Enum.BookingStatus;
import com.sqts.sbvms.Model.TimeSlot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    @JoinColumn(name = "vendor_service_id")
    @ManyToOne
    private VendorService vendorService;
    @NotNull
    private LocalDate bookingDate;
    @Embedded
    @NotNull
    private TimeSlot timeSlot;
    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @NotNull
    @ManyToOne
    private ServiceCategory serviceCategory;
}
