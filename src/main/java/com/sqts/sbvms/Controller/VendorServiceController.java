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
    @PostMapping("/vendor/register")
    public ResponseEntity<ApiResponse<VendorRegistrationResponse>> registerVendor(
            @Valid @RequestBody VendorRegistrationRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Registration request submitted successfully. Please wait for admin approval.",
                        vendorServiceService.registerVendor(request),
                        LocalDateTime.now()
                ),
                HttpStatus.CREATED
        );
    }
    @GetMapping("/vendors/pending")
    public ResponseEntity<ApiResponse<List<PendingVendorResponse>>> getPendingVendors() {

        List<PendingVendorResponse> responses =
                vendorServiceService.getPendingVendors();

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        responses.isEmpty()
                                ? "No pending vendor requests."
                                : "Pending vendors found.",
                        responses,
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @GetMapping("/vendors/{id}/verification")
    public ResponseEntity<ApiResponse<VendorVerificationResponse>> getVendorVerificationDetails(
            @PathVariable("id") Long vendorId) {

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor details found.",
                        vendorServiceService.getVendorVerificationDetails(vendorId),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @PatchMapping("/vendors/{id}/approve")
    public ResponseEntity<ApiResponse<String>> approveVendor(
            @PathVariable("id") Long vendorId){

        vendorServiceService.approveVendor(vendorId);

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor approved successfully.",
                        null,
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @PatchMapping("/vendors/{id}/reject")
    public ResponseEntity<ApiResponse<String>> rejectVendor(
            @PathVariable("id") Long vendorId){

        vendorServiceService.rejectVendor(vendorId);

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor registration rejected.",
                        null,
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
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
    @GetMapping("/vendors/search")
    public ResponseEntity<ApiResponse<List<SearchResponse>>> searchVendorServicesByVendorOrService(@RequestParam(name = "vendorName", required = false) String vendorName,
                                                                                        @RequestParam(name = "serviceName", required = false) String serviceName){
        List<SearchResponse> responses = vendorServiceService.searchVendorServicesByVendorOrService(vendorName, serviceName);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        responses.isEmpty() ? "No data found" : "Data found",
                        responses,
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/vendor/{vendorId}/services/{serviceId}")
    public ResponseEntity<ApiResponse<SearchResponse>> getSingleAssignedServiceOfVendor(@PathVariable(name = "vendorId") Long vendorId,
                                                                                        @PathVariable(name = "serviceId") Long serviceId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found",
                        vendorServiceService.getSingleAssignedServiceOfVendor(vendorId, serviceId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/vendor/{id}/summary")
    public ResponseEntity<ApiResponse<VendorSummaryResponse>> viewDashboardSummary(@PathVariable(name = "id") Long vendorId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found",
                        vendorServiceService.viewDashboardSummary(vendorId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/vendors/count")
    public ResponseEntity<ApiResponse<Long>> countTotalVendors(){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found",
                        vendorServiceService.countTotalVendors(),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @PostMapping("/vendor/{id}/services/bulk")
    public ResponseEntity<ApiResponse<List<ServiceAssignmentResponse>>> bulkServiceAssignment(@PathVariable("id") Long vendorId,
                                                                      @RequestBody List<BulkServiceAssignmentRequest> request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "services assigned successfully.",
                        vendorServiceService.bulkServiceAssignment(vendorId, request),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<VendorProfileResponse>> getMyProfile() {

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Profile found.",
                        vendorServiceService.getMyProfile(),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<VendorUpdateResponse>> updateMyProfile(
            @Valid @RequestBody VendorUpdateRequest request) {

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Profile updated successfully.",
                        vendorServiceService.updateMyProfile(request),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @GetMapping("/me/dashboard")
    public ResponseEntity<ApiResponse<VendorSummaryResponse>> getMyDashboard() {

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        vendorServiceService.getMyDashboard(),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @GetMapping("/me/services")
    public ResponseEntity<ApiResponse<DisplayVendorDetails>> getMyServices(){

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        vendorServiceService.getMyServices(),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
    @GetMapping("/me/services/{serviceId}")
    public ResponseEntity<ApiResponse<SearchResponse>> getMyService(
            @PathVariable Long serviceId){

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        vendorServiceService.getMyService(serviceId),
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }
}
