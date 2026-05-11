package com.sqts.sbvms.Repository;

import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Entity.VendorService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorServiceRepository extends JpaRepository<VendorService, Long> {
    List<VendorService> findByVendorId(Long id);
    boolean existsByVendorIdAndServiceCategoryId(Long vendorId, Long serviceCategoryId);
}
