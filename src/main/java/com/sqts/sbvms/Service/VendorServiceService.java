package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Entity.User;
import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Entity.VendorService;
import com.sqts.sbvms.Enum.Role;
import com.sqts.sbvms.Exception.*;
import com.sqts.sbvms.Repository.ServiceCategoryRepository;
import com.sqts.sbvms.Repository.UserRepository;
import com.sqts.sbvms.Repository.VendorRepository;
import com.sqts.sbvms.Repository.VendorServiceRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VendorServiceService {
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final VendorServiceRepository vendorServiceRepository;
    public VendorServiceService(VendorRepository vendorRepository,
                                UserRepository userRepository,
                                ServiceCategoryRepository serviceCategoryRepository,
                                VendorServiceRepository vendorServiceRepository) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.vendorServiceRepository = vendorServiceRepository;
    }
    @Transactional
    public VendorCreationResponse createVendor(VendorCreationRequest request){
        if(request == null)
            throw new InvalidInputException("Please fill all the details.");
        if(request.getPassword().length() < 8)
            throw new WeakPasswordException("Provided password is weak.");
        if(userRepository.existsByEmail(request.getEmail().trim()))
            throw new UserAlreadyExistsException("Email already registered.");

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim());
        user.setPassword(request.getPassword().trim());
        user.setRole(Role.VENDOR);
        userRepository.save(user);

        Vendor vendor = new Vendor();
        vendor.setUser(user);
        vendorRepository.save(vendor);

        VendorCreationResponse response = new VendorCreationResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setVendorId(vendor.getId());

        return response;
    }
    @Transactional
    public ServiceAssignmentResponse assignServiceToVendor(ServiceAssignmentRequest request) {
        if(request == null)
            throw new InvalidInputException("Please fill all the fields.");

        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        ServiceCategory serviceCategory = serviceCategoryRepository.findById(request.getServiceCategoryId())
                .orElseThrow(() ->
                        new ServiceNotFoundException("Service not found."));

        if(vendorServiceRepository.existsByVendorIdAndServiceCategoryId(request.getVendorId(), request.getServiceCategoryId()))
            throw new DuplicateServiceAssignmentException("Provided service already mapped to this vendor.");

        VendorService vendorService = new VendorService();
        vendorService.setVendor(vendor);
        vendorService.setServiceCategory(serviceCategory);
        vendorService.setPrice(request.getPrice());
        vendorService.setDuration(request.getDuration());
        vendorServiceRepository.save(vendorService);

        ServiceAssignmentResponse response = new ServiceAssignmentResponse();
        response.setVendorId(vendor.getId());
        response.setServiceCategoryName(serviceCategory.getServiceName());
        response.setPrice(request.getPrice());
        response.setDuration(request.getDuration());
        return response;
    }
    public DisplayVendorDetails displayVendor(Long id) {
        Optional<Vendor> vendorOpt = vendorRepository.findById(id);
        if (vendorOpt.isEmpty())
            throw new NoVendorFoundException("Vendor not found.");
        User user = vendorOpt.get().getUser();
        String name = user.getName();
        String email = user.getEmail();
        DisplayVendorDetails displayVendorDetails = new DisplayVendorDetails();
        List<VendorService> providedServices = vendorServiceRepository.findByVendorId(id);
        List<VendorServiceDetails> serviceDetails = new ArrayList<>();
        for (VendorService service : providedServices) {
            VendorServiceDetails vendorServiceDetails = new VendorServiceDetails();
            long price = service.getPrice();
            Duration duration = service.getDuration();
            ServiceCategory serviceCategory = service.getServiceCategory();
            vendorServiceDetails.setPrice(price);
            vendorServiceDetails.setDuration(duration);
            vendorServiceDetails.setServiceCategory(serviceCategory);
            serviceDetails.add(vendorServiceDetails);
        }
        displayVendorDetails.setVendorName(name);
        displayVendorDetails.setVendorEmail(email);
        displayVendorDetails.setVendorServiceDetails(serviceDetails);
        return displayVendorDetails;
    }
}
