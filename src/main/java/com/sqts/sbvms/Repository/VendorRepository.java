package com.sqts.sbvms.Repository;

import com.sqts.sbvms.Entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
