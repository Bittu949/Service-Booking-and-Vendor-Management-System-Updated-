package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Entity.User;
import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Entity.VendorService;
import com.sqts.sbvms.Service.VendorServiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class VendorServiceController {
    VendorServiceService vendorServiceService;
    public VendorServiceController(VendorServiceService vendorServiceService) {
        this.vendorServiceService = vendorServiceService;
    }
    @PostMapping("/vendor")
    public ResponseEntity<ApiResponse<VendorCreationResponse>> createVendor(@Valid @RequestBody VendorCreationRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor created successfully.",
                        vendorServiceService.createVendor(request),
                        LocalDateTime.now()),
                HttpStatus.CREATED);
    }
    @PostMapping("/vendor/assignService")
    public ResponseEntity<ApiResponse<ServiceAssignmentResponse>> assignServiceToVendor(@Valid @RequestBody ServiceAssignmentRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service assigned to vendor successfully.",
                        vendorServiceService.assignServiceToVendor(request),
                        LocalDateTime.now()),
                HttpStatus.CREATED);
    }
    @GetMapping("/vendor")
    public ResponseEntity<ApiResponse<List<DisplayVendorDetails>>> displayAllVendors(){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        vendorServiceService.displayAllVendors(),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/vendor/{id}/services")
    public ResponseEntity<ApiResponse<DisplayVendorDetails>> viewAssignedServices(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        vendorServiceService.viewAssignedServices(id),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/vendor/{id}")
    public ResponseEntity<ApiResponse<DisplayVendorDetails>> displayVendor(@PathVariable Long id){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        vendorServiceService.displayVendor(id),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @PutMapping("/vendor/{id}")
    public ResponseEntity<ApiResponse<VendorUpdateResponse>> updateVendor(@PathVariable(name = "id") Long vendorId,
                                                                          @Valid @RequestBody VendorUpdateRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor updated successfully.",
                        vendorServiceService.updateVendor(vendorId, request),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @DeleteMapping("/vendor/{id}")
    public ResponseEntity<ApiResponse<VendorDeletionResponse>> deleteVendor(@PathVariable(name = "id") long vendorId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor deleted successfully.",
                        vendorServiceService.deleteVendor(vendorId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @DeleteMapping("/vendor/{vendorId}/services/{serviceId}")
    public ResponseEntity<ApiResponse<String>> removeAssignedServiceFromVendor(@PathVariable(name = "vendorId") long vendorId,
                                                                               @PathVariable(name = "serviceId") long serviceId){
        vendorServiceService.removeAssignedServiceFromVendor(vendorId, serviceId);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service removed from vendor successfully.",
                        null,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @PutMapping("/vendor/{vendorId}/services/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceUpdationResponse>> updateAssignedServiceDetails(@PathVariable(name = "vendorId") long vendorId,
                                                                                   @PathVariable(name = "serviceId") long serviceId,
                                                                                   @RequestBody ServiceUpdationRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service updated successfully.",
                        vendorServiceService.updateAssignedServiceDetails(vendorId, serviceId, request),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
}
