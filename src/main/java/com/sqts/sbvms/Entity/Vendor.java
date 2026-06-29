package com.sqts.sbvms.Entity;

import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Model.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "vendor",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<VendorService> vendorServices;
    @Enumerated(EnumType.STRING)
    private VendorStatus status;
    @Embedded
    Address vendorAddress;
    @NotNull
    private Integer experienceYears;
    @Column(length = 1000)
    private String description;
    @NotBlank
    @Column(unique = true)
    private String phoneNumber;
    @NotNull
    @Column(unique = true)
    private String aadhaarNumber;
    @NotNull
    private String aadhaarFrontImage;
    @NotNull
    private String aadhaarBackImage;
    @NotNull
    private String verificationDocument;
}
