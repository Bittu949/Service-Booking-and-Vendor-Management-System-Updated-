package com.sqts.sbvms.Repository;

import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Enum.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByUserId(Long userId);
    List<Vendor> findByStatus(VendorStatus status);
    boolean existsByPhoneNumber(String phoneNumber);
}
