package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.ApiResponse;
import com.sqts.sbvms.Dto.VendorByServiceResponse;
import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Service.ServiceCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ServiceCategoryController {
    private final ServiceCategoryService serviceCategoryService;
    public ServiceCategoryController(ServiceCategoryService serviceCategoryService){
        this.serviceCategoryService = serviceCategoryService;
    }
    @PostMapping("/services")
    public ResponseEntity<ApiResponse<ServiceCategory>> createService(@Valid @RequestBody ServiceCategory service){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service created.",
                        serviceCategoryService.createService(service),
                        LocalDateTime.now()),
                HttpStatus.CREATED);
    }
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<ServiceCategory>>> displayServices(){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Services found.",
                        serviceCategoryService.displayServices(),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @PutMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceCategory>> updateService(@PathVariable Long id,
                                                                      @RequestBody ServiceCategory updatedService){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service updated.",
                        serviceCategoryService.updateService(id, updatedService),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @DeleteMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceCategory>> deleteService(@PathVariable(name = "id") Long serviceId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service deleted.",
                        serviceCategoryService.deleteService(serviceId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceCategory>> getSingleServices(@PathVariable(name = "id") Long serviceId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service found.",
                        serviceCategoryService.getSingleServices(serviceId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/services/{id}/vendors")
    public ResponseEntity<ApiResponse<List<VendorByServiceResponse>>> getVendorsByService(@PathVariable(name = "id") Long serviceId,
                                                                                          @RequestParam(name = "minPrice", required = false) Long minPrice,
                                                                                          @RequestParam(name = "maxPrice", required = false) Long maxPrice){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        serviceCategoryService.getVendorsByService(serviceId, minPrice, maxPrice),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
}
