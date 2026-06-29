package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.*;
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
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>> createService(
            @Valid @RequestBody ServiceCategoryRequest request) {

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service created.",
                        serviceCategoryService.createService(request),
                        LocalDateTime.now()),
                HttpStatus.CREATED);
    }
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<ServiceCategoryResponse>>> displayServices(){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Services found.",
                        serviceCategoryService.displayServices(),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @PatchMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>> updateService(
            @PathVariable Long id,
            @RequestBody ServiceCategoryUpdateRequest request) {
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service updated.",
                        serviceCategoryService.updateService(id, request),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @DeleteMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>> deleteService(@PathVariable(name = "id") Long serviceId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service deleted.",
                        serviceCategoryService.deleteService(serviceId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>> getServiceById(@PathVariable(name = "id") Long serviceId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service found.",
                        serviceCategoryService.getServiceById(serviceId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/services/{id}/vendors")
    public ResponseEntity<ApiResponse<List<VendorByServiceResponse>>> getVendorsByService(@PathVariable(name = "id") Long serviceId,
                                                                                          @RequestParam(name = "minPrice", required = false) Long minPrice,
                                                                                          @RequestParam(name = "maxPrice", required = false) Long maxPrice,
                                                                                          @RequestParam(name = "sortBy", required = false) String sortBy){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        serviceCategoryService.getVendorsByService(serviceId, minPrice, maxPrice, sortBy),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/services/count")
    public ResponseEntity<ApiResponse<Long>> countTotalServices(){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found",
                        serviceCategoryService.countTotalServices(),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
}
