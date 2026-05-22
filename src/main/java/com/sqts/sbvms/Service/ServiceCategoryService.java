package com.sqts.sbvms.Service;

import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Exception.InvalidInputException;
import com.sqts.sbvms.Exception.NoServiceFoundException;
import com.sqts.sbvms.Repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceCategoryService {
    ServiceCategoryRepository serviceCategoryRepository;
    public ServiceCategoryService(ServiceCategoryRepository serviceCategoryRepository) {
        this.serviceCategoryRepository = serviceCategoryRepository;
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
}
