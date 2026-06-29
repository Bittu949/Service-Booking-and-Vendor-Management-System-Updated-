package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.ServiceCategoryRequest;
import com.sqts.sbvms.Dto.ServiceCategoryResponse;
import com.sqts.sbvms.Dto.ServiceCategoryUpdateRequest;
import com.sqts.sbvms.Dto.VendorByServiceResponse;
import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Entity.VendorService;
import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Exception.*;
import com.sqts.sbvms.Repository.ServiceCategoryRepository;
import com.sqts.sbvms.Repository.VendorServiceRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ServiceCategoryService {
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final VendorServiceRepository vendorServiceRepository;
    public ServiceCategoryService(ServiceCategoryRepository serviceCategoryRepository,
                                  VendorServiceRepository vendorServiceRepository) {
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.vendorServiceRepository = vendorServiceRepository;
    }
    public ServiceCategoryResponse createService(ServiceCategoryRequest request){

        if(request == null)
            throw new InvalidInputException("Please fill all the fields.");

        String serviceName = request.getServiceName().trim();

        if(serviceCategoryRepository.existsByServiceNameIgnoreCase(serviceName))
            throw new DuplicateServiceException("Service already exists.");

        ServiceCategory service = new ServiceCategory();
        service.setServiceName(serviceName);
        service.setDescription(request.getDescription().trim());

        serviceCategoryRepository.save(service);

        ServiceCategoryResponse response = new ServiceCategoryResponse();
        response.setId(service.getId());
        response.setServiceName(service.getServiceName());
        response.setDescription(service.getDescription());

        return response;
    }
    public List<ServiceCategoryResponse> displayServices() {
        List<ServiceCategory> services = serviceCategoryRepository.findAll();
        if (services.isEmpty())
            throw new NoServiceFoundException("No service found.");
        List<ServiceCategoryResponse> responses = new ArrayList<>();
        for (ServiceCategory service : services) {

            ServiceCategoryResponse response = new ServiceCategoryResponse();

            response.setId(service.getId());
            response.setServiceName(service.getServiceName());
            response.setDescription(service.getDescription());

            responses.add(response);
        }

        return responses;
    }
    public ServiceCategoryResponse updateService(
            Long id,
            ServiceCategoryUpdateRequest request){

        ServiceCategory service = serviceCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new NoServiceFoundException("Service not found."));

        if(request == null)
            throw new InvalidInputException("Please fill all the fields.");

        if(request.getServiceName() != null){

            String serviceName = request.getServiceName().trim();

            if(!serviceName.equalsIgnoreCase(service.getServiceName())
                    && serviceCategoryRepository.existsByServiceNameIgnoreCase(serviceName))
                throw new DuplicateServiceException("Service already exists.");

            service.setServiceName(serviceName);
        }

        if(request.getDescription() != null)
            service.setDescription(request.getDescription().trim());

        serviceCategoryRepository.save(service);

        ServiceCategoryResponse response = new ServiceCategoryResponse();

        response.setId(service.getId());
        response.setServiceName(service.getServiceName());
        response.setDescription(service.getDescription());

        return response;
    }
    public ServiceCategoryResponse deleteService(Long serviceId){

        ServiceCategory service = serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() ->
                        new NoServiceFoundException("Service not found."));

        if(vendorServiceRepository.existsByServiceCategoryId(serviceId))
            throw new InvalidOperationException(
                    "Cannot delete a service that is assigned to vendors.");

        serviceCategoryRepository.delete(service);

        ServiceCategoryResponse response = new ServiceCategoryResponse();
        response.setId(service.getId());
        response.setServiceName(service.getServiceName());
        response.setDescription(service.getDescription());

        return response;
    }
    public ServiceCategoryResponse getServiceById(Long serviceId){

        ServiceCategory service = serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() ->
                        new NoServiceFoundException("Service not found."));

        ServiceCategoryResponse response = new ServiceCategoryResponse();

        response.setId(service.getId());
        response.setServiceName(service.getServiceName());
        response.setDescription(service.getDescription());

        return response;
    }
    public List<VendorByServiceResponse> getVendorsByService(Long serviceId, Long minPrice, Long maxPrice, String sortBy){
        ServiceCategory service = serviceCategoryRepository.findById(serviceId)
                                    .orElseThrow(() -> new ServiceNotFoundException("Service not found."));
        List<VendorService> vendorServices = vendorServiceRepository.findByServiceCategory(service);
        if(vendorServices.isEmpty())
            throw new ServiceAssignmentNotFoundException("No vendor provides this service.");
        if(minPrice != null && maxPrice != null) {
            if (minPrice > maxPrice)
                throw new InvalidInputException("Min price cannot be greater than max price.");
        }

        if(minPrice != null)
            vendorServices = new ArrayList<>(vendorServices.stream().filter(v -> v.getPrice() >= minPrice).toList());
        if(maxPrice != null)
            vendorServices = new ArrayList<>(vendorServices.stream().filter(v -> v.getPrice() <= maxPrice).toList());

        if (sortBy != null) {
            if (sortBy.equalsIgnoreCase("price")) {
                vendorServices.sort(Comparator.comparing(VendorService::getPrice));
            }
            else if (sortBy.equalsIgnoreCase("name")) {
                vendorServices.sort(
                        Comparator.comparing(v -> v.getVendor().getUser().getName()));
            }
            else {
                throw new InvalidInputException(
                        "sortBy must be either 'price' or 'name'.");
            }
        }

        List<VendorByServiceResponse> vendorByServiceResponses = new ArrayList<>();
        for(VendorService vendorService : vendorServices){
            if(vendorService.getVendor().getStatus() == VendorStatus.ACTIVE) {
                VendorByServiceResponse response = new VendorByServiceResponse();
                response.setVendorId(vendorService.getVendor().getId());
                response.setVendorName(vendorService.getVendor().getUser().getName());
                response.setVendorEmail(vendorService.getVendor().getUser().getEmail());
                response.setPrice(vendorService.getPrice());
                response.setDuration(vendorService.getDuration());
                response.setVendorAddress(vendorService.getVendor().getVendorAddress());
                vendorByServiceResponses.add(response);
            }
        }
        if(vendorByServiceResponses.isEmpty())
            throw new ServiceAssignmentNotFoundException(
                    "No active vendor provides this service.");
        return vendorByServiceResponses;
    }
    public Long countTotalServices(){
        return serviceCategoryRepository.count();
    }
}
