package com.sqts.sbvms.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Entity
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
    Vendor vendor;
    @ManyToOne
    @JoinColumn(name = "service_category_id")
    @NotNull
    ServiceCategory serviceCategory;
    @NotNull
    Long price;
    @NotNull
    Duration duration;
}
