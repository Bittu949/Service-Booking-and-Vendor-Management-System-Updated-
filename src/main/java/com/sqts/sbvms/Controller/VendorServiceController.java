package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Entity.User;
import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Entity.VendorService;
import com.sqts.sbvms.Service.VendorServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@Tag(
        name = "Vendor Management",
        description = "APIs for vendor profile management, approval, service assignment and administration."
)
@RestController
public class VendorServiceController {
    VendorServiceService vendorServiceService;
    public VendorServiceController(VendorServiceService vendorServiceService) {
        this.vendorServiceService = vendorServiceService;
    }
    @Operation(
            summary = "Register as Vendor",
            description = "Allows a new vendor to submit a registration request. The registration remains in pending approval status until reviewed by an administrator."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Vendor registration submitted successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or duplicate email/phone number."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Vendor already exists.")
    })
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
    @Operation(
            summary = "Get Pending Vendor Requests",
            description = "Retrieves all vendor registration requests that are waiting for administrator approval."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pending vendors retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No pending vendor requests found.")
    })
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
    @Operation(
            summary = "View Vendor Verification Details",
            description = "Retrieves complete vendor registration details including uploaded verification documents for administrator review."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor verification details retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @GetMapping("/vendors/{id}/verification")
    public ResponseEntity<ApiResponse<VendorVerificationResponse>> getVendorVerificationDetails(
            @Parameter(
                    description = "Vendor ID",
                    required = true,
                    example = "1"
            )
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
    @Operation(
            summary = "Approve Vendor",
            description = "Approves a pending vendor registration request and changes the vendor status to ACTIVE."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor approved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Vendor is not in pending approval state."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @PatchMapping("/vendors/{id}/approve")
    public ResponseEntity<ApiResponse<String>> approveVendor(
            @Parameter(
                    description = "Vendor ID",
                    required = true,
                    example = "1"
            )
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
    @Operation(
            summary = "Reject Vendor",
            description = "Rejects a pending vendor registration request."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor rejected successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Vendor is not in pending approval state."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @PatchMapping("/vendors/{id}/reject")
    public ResponseEntity<ApiResponse<String>> rejectVendor(
            @Parameter(
                    description = "Vendor ID",
                    required = true,
                    example = "1"
            )
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
    @Operation(
            summary = "Assign Service to Vendor",
            description = "Assigns a service category to a vendor along with the service price and estimated completion duration."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Service assigned successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or service already assigned."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor or service category not found.")
    })
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
    @Operation(
            summary = "Get All Vendors",
            description = "Retrieves all vendors registered in the system along with their profile details."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendors retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No vendors found.")
    })
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
    @Operation(
            summary = "Get Vendor Services",
            description = "Retrieves all services currently assigned to the specified vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Assigned services retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @GetMapping("/vendor/{id}/services")
    public ResponseEntity<ApiResponse<DisplayVendorDetails>> viewAssignedServices(
            @Parameter(
                    description = "Vendor ID",
                    example = "1",
                    required = true
            )
            @PathVariable(name = "id") Long id){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        vendorServiceService.viewAssignedServices(id),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Get Vendor Details",
            description = "Retrieves complete profile information for a specific vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @GetMapping("/vendor/{id}")
    public ResponseEntity<ApiResponse<DisplayVendorDetails>> displayVendor(
            @Parameter(
                    description = "Vendor ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long id){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        vendorServiceService.displayVendor(id),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Update Vendor",
            description = "Updates the profile information of an existing vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor updated successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid vendor details."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @PutMapping("/vendor/{id}")
    public ResponseEntity<ApiResponse<VendorUpdateResponse>> updateVendor(
            @Parameter(
                    description = "Vendor ID",
                    example = "1",
                    required = true
            )
            @PathVariable(name = "id") Long vendorId,
            @Valid @RequestBody VendorUpdateRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor updated successfully.",
                        vendorServiceService.updateVendor(vendorId, request),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Delete Vendor",
            description = "Deletes a vendor from the system if the vendor has no active business dependencies."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor deleted successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Vendor cannot be deleted."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @DeleteMapping("/vendor/{id}")
    public ResponseEntity<ApiResponse<VendorDeletionResponse>> deleteVendor(
            @Parameter(
                    description = "Vendor ID",
                    example = "1",
                    required = true
            )
            @PathVariable(name = "id") long vendorId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Vendor deleted successfully.",
                        vendorServiceService.deleteVendor(vendorId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Remove Vendor Service",
            description = "Removes a previously assigned service from the specified vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service removed successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor or assigned service not found.")
    })
    @DeleteMapping("/vendor/{vendorId}/services/{serviceId}")
    public ResponseEntity<ApiResponse<String>> removeAssignedServiceFromVendor(
            @Parameter(
                    description = "Vendor ID",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "vendorId") long vendorId,
            @Parameter(
                    description = "Service Category ID",
                    required = true,
                    example = "2"
            )
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
    @Operation(
            summary = "Update Vendor Service",
            description = "Updates the price and estimated duration of a service assigned to a vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Assigned service updated successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid service details."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor or assigned service not found.")
    })
    @PutMapping("/vendor/{vendorId}/services/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceUpdationResponse>> updateAssignedServiceDetails(
            @Parameter(
                    description = "Vendor ID",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "vendorId") long vendorId,
            @Parameter(
                    description = "Service Category ID",
                    required = true,
                    example = "2"
            )
            @PathVariable(name = "serviceId") long serviceId,
            @Valid @RequestBody ServiceUpdationRequest request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service updated successfully.",
                        vendorServiceService.updateAssignedServiceDetails(vendorId, serviceId, request),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Search Vendor Services",
            description = "Searches vendors by vendor name, service name or a combination of both."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search completed successfully.")
    })
    @GetMapping("/vendors/search")
    public ResponseEntity<ApiResponse<List<SearchResponse>>> searchVendorServicesByVendorOrService(
            @Parameter(
                    description = "Vendor name to search",
                    example = "Rahul"
            )
            @RequestParam(name = "vendorName", required = false) String vendorName,
            @Parameter(
                    description = "Service category name to search",
                    example = "Plumbing"
            )
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
    @Operation(
            summary = "Get Vendor Service",
            description = "Retrieves details of a specific service assigned to a vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Assigned service retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor or assigned service not found.")
    })
    @GetMapping("/vendor/{vendorId}/services/{serviceId}")
    public ResponseEntity<ApiResponse<SearchResponse>> getSingleAssignedServiceOfVendor(
            @Parameter(
                    description = "Vendor ID",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "vendorId") Long vendorId,
            @Parameter(
                    description = "Service Category ID",
                    required = true,
                    example = "2"
            )
            @PathVariable(name = "serviceId") Long serviceId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found",
                        vendorServiceService.getSingleAssignedServiceOfVendor(vendorId, serviceId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Get Vendor Dashboard Summary",
            description = "Retrieves booking and service statistics for the specified vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dashboard summary retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
    @GetMapping("/vendor/{id}/summary")
    public ResponseEntity<ApiResponse<VendorSummaryResponse>> viewDashboardSummary(
            @Parameter(
                    description = "Vendor ID",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long vendorId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found",
                        vendorServiceService.viewDashboardSummary(vendorId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Get Total Vendors",
            description = "Returns the total number of vendors registered in the system."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor count retrieved successfully.")
    })
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
    @Operation(
            summary = "Assign Multiple Services",
            description = "Assigns multiple service categories to a vendor in a single request."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Services assigned successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor or service category not found.")
    })
    @PostMapping("/vendor/{id}/services/bulk")
    public ResponseEntity<ApiResponse<List<ServiceAssignmentResponse>>> bulkServiceAssignment(
            @Parameter(
                    description = "Vendor ID",
                    required = true,
                    example = "1"
            )
            @PathVariable("id") Long vendorId,
            @Valid @RequestBody List<BulkServiceAssignmentRequest> request){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Services assigned successfully.",
                        vendorServiceService.bulkServiceAssignment(vendorId, request),
                        LocalDateTime.now()),
                HttpStatus.CREATED);
    }
    @Operation(
            summary = "Get My Profile",
            description = "Retrieves the profile information of the authenticated vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor profile retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
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
    @Operation(
            summary = "Update My Profile",
            description = "Allows the authenticated vendor to update their editable profile information."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendor profile updated successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid profile details."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vendor not found.")
    })
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
    @Operation(
            summary = "Get My Dashboard",
            description = "Retrieves booking statistics and dashboard summary for the authenticated vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dashboard summary retrieved successfully.")
    })
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
    @Operation(
            summary = "Get My Services",
            description = "Retrieves all services currently offered by the authenticated vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Assigned services retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No assigned services found.")
    })
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
    @Operation(
            summary = "Get My Service",
            description = "Retrieves details of a specific service offered by the authenticated vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Assigned service not found.")
    })
    @GetMapping("/me/services/{serviceId}")
    public ResponseEntity<ApiResponse<SearchResponse>> getMyService(
            @Parameter(
                    description = "Service Category ID",
                    required = true,
                    example = "2"
            )
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
