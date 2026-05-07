package com.sqts.sbvms.Service;

import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Exception.InvalidInputException;
import com.sqts.sbvms.Exception.NoServiceFoundException;
import com.sqts.sbvms.Repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
