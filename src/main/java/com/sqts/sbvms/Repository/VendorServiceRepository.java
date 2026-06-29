package com.sqts.sbvms.Repository;

import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Entity.VendorService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorServiceRepository extends JpaRepository<VendorService, Long> {
    List<VendorService> findByVendorId(Long id);
    boolean existsByVendorIdAndServiceCategoryId(Long vendorId, Long serviceCategoryId);
    void removeByVendor_idAndServiceCategory_id(Long vendorId, Long serviceId);
    VendorService findByVendor_idAndServiceCategory_id(Long vendorId, Long serviceId);
    List<VendorService> findByServiceCategory(ServiceCategory service);
    Long countByVendor_id(Long vendorId);
    List<VendorService> findByServiceCategoryId(Long serviceId);
    boolean existsByServiceCategoryId(Long serviceCategoryId);
}
