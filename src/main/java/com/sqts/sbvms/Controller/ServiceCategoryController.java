package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.ApiResponse;
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
}
