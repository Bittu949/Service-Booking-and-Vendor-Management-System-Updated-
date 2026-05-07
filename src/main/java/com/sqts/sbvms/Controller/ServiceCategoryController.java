package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.ApiResponse;
import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Service.ServiceCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
}
