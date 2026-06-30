package com.sqts.sbvms.Controller;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Service.ServiceCategoryService;
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
        name = "Service Management",
        description = "APIs for managing service categories and browsing available services."
)
@RestController
public class ServiceCategoryController {
    private final ServiceCategoryService serviceCategoryService;
    public ServiceCategoryController(ServiceCategoryService serviceCategoryService){
        this.serviceCategoryService = serviceCategoryService;
    }
    @Operation(
            summary = "Create Service Category",
            description = "Creates a new service category that vendors can later offer and customers can book."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Service category created successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid service category details."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Service category already exists.")
    })
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
    @Operation(
            summary = "Get All Service Categories",
            description = "Retrieves all available service categories in the system."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service categories retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No service categories found.")
    })
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
    @Operation(
            summary = "Update Service Category",
            description = "Updates the name or description of an existing service category."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service category updated successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid service category details."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Service category not found."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Service category already exists.")
    })
    @PatchMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>> updateService(
            @Parameter(
                    description = "Service Category ID",
                    required = true,
                    example = "1"
            )
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
    @Operation(
            summary = "Delete Service Category",
            description = "Deletes a service category if it is not currently assigned to any vendor."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service category deleted successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Service category is assigned to vendors and cannot be deleted."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Service category not found.")
    })
    @DeleteMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>> deleteService(
            @Parameter(
                    description = "Service Category ID",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long serviceId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service deleted.",
                        serviceCategoryService.deleteService(serviceId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Get Service Category by ID",
            description = "Retrieves the details of a specific service category."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service category retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Service category not found.")
    })
    @GetMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>> getServiceById(
            @Parameter(
                    description = "Service Category ID",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long serviceId){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Service found.",
                        serviceCategoryService.getServiceById(serviceId),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Get Vendors by Service Category",
            description = "Retrieves all active vendors offering the specified service with optional price filtering and sorting."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vendors retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid filter or sorting parameters."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Service category not found or no vendors available.")
    })
    @GetMapping("/services/{id}/vendors")
    public ResponseEntity<ApiResponse<List<VendorByServiceResponse>>> getVendorsByService(
            @Parameter(
                    description = "Service Category ID",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long serviceId,
            @Parameter(
                    description = "Minimum service price",
                    example = "500"
            )
            @RequestParam(name = "minPrice", required = false) Long minPrice,
            @Parameter(
                    description = "Maximum service price",
                    example = "3000"
            )
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @Parameter(
                    description = "Sort vendors by 'price' or 'name'",
                    example = "price"
            )
            @RequestParam(name = "sortBy", required = false) String sortBy){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        "Data found.",
                        serviceCategoryService.getVendorsByService(serviceId, minPrice, maxPrice, sortBy),
                        LocalDateTime.now()),
                HttpStatus.OK);
    }
    @Operation(
            summary = "Get Service Category Count",
            description = "Returns the total number of service categories available in the system."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service category count retrieved successfully.")
    })
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
