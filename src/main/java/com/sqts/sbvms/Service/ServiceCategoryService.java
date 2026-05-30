package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.VendorByServiceResponse;
import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Entity.VendorService;
import com.sqts.sbvms.Exception.*;
import com.sqts.sbvms.Repository.ServiceCategoryRepository;
import com.sqts.sbvms.Repository.VendorServiceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceCategoryService {
    ServiceCategoryRepository serviceCategoryRepository;
    VendorServiceRepository vendorServiceRepository;
    public ServiceCategoryService(ServiceCategoryRepository serviceCategoryRepository,
                                  VendorServiceRepository vendorServiceRepository) {
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.vendorServiceRepository = vendorServiceRepository;
    }
    public ServiceCategory createService(ServiceCategory service){
        if(service==null)
            throw new InvalidInputException("Please fill all the fields.");
        return serviceCategoryRepository.save(service);
    }
    public List<ServiceCategory> displayServices(){
        List<ServiceCategory> services = serviceCategoryRepository.findAll();
        if(services.isEmpty())
            throw new NoServiceFoundException("No service found.");
        return services;
    }
    public ServiceCategory updateService(Long id, ServiceCategory updatedService){
        Optional<ServiceCategory> serviceOpt = serviceCategoryRepository.findById(id);
        if(serviceOpt.isEmpty())
            throw new NoServiceFoundException("No service found");
        ServiceCategory service = serviceOpt.get();
        if(updatedService.getServiceName()!=null && !updatedService.getServiceName().isEmpty())
            service.setServiceName(updatedService.getServiceName());
        if(updatedService.getDescription()!=null && !updatedService.getDescription().isEmpty())
            service.setDescription(updatedService.getDescription());
        serviceCategoryRepository.save(service);
        return service;
    }
    public ServiceCategory deleteService(Long serviceId){
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() -> new NoServiceFoundException("Service not found."));
        serviceCategoryRepository.delete(serviceCategory);
        return serviceCategory;
    }
    public ServiceCategory getSingleServices(Long serviceId){
        return serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() -> new NoServiceFoundException("Service not found."));
    }
    public List<VendorByServiceResponse> getVendorsByService(Long serviceId, Long minPrice, Long maxPrice){
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

        vendorServices.sort(Comparator.comparing(VendorService::getPrice));
        List<VendorByServiceResponse> vendorByServiceResponses = new ArrayList<>();
        for(VendorService vendorService : vendorServices){
            VendorByServiceResponse response = new VendorByServiceResponse();
            response.setVendorId(vendorService.getVendor().getId());
            response.setVendorName(vendorService.getVendor().getUser().getName());
            response.setVendorEmail(vendorService.getVendor().getUser().getEmail());
            response.setPrice(vendorService.getPrice());
            response.setDuration(vendorService.getDuration());
            vendorByServiceResponses.add(response);
        }
        return vendorByServiceResponses;
    }
}
