package com.sqts.sbvms.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"vendor_id", "service_category_id"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "vendor_id")
    Vendor vendor;
    @ManyToOne
    @JoinColumn(name = "service_category_id")
    @NotNull
    ServiceCategory serviceCategory;
    @NotNull
    @Positive
    Long price;
    @NotNull
    Duration duration;
}
