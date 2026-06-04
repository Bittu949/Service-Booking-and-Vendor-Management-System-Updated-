package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.*;
import com.sqts.sbvms.Entity.ServiceCategory;
import com.sqts.sbvms.Entity.User;
import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Entity.VendorService;
import com.sqts.sbvms.Enum.Role;
import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Exception.*;
import com.sqts.sbvms.Repository.ServiceCategoryRepository;
import com.sqts.sbvms.Repository.UserRepository;
import com.sqts.sbvms.Repository.VendorRepository;
import com.sqts.sbvms.Repository.VendorServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
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
        vendor.setStatus(VendorStatus.ACTIVE);
        vendorRepository.save(vendor);

        VendorCreationResponse response = new VendorCreationResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setVendorId(vendor.getId());

        return response;
    }
    public ServiceAssignmentResponse assignServiceToVendor(ServiceAssignmentRequest request) {
        if(request == null)
            throw new InvalidInputException("Please fill all the fields.");

        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        if(vendor.getStatus() != VendorStatus.ACTIVE)
            throw new InvalidVendorStatusException("Only active vendors can be assigned services.");

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
    public List<DisplayVendorDetails> displayAllVendors(){
        List<DisplayVendorDetails> vendorDetailsList = new ArrayList<>();
        List<Vendor> vendorList = vendorRepository.findAll();
        vendorList = vendorList.stream()
                .filter(v -> v.getStatus() == VendorStatus.ACTIVE)
                .toList();
        if (vendorList.isEmpty())
            throw new NoVendorFoundException("No vendor found.");
        for(Vendor vendor : vendorList) {
            List<VendorService> providedServicesByVendor = vendor.getVendorServices();
            List<VendorServiceDetails> serviceDetails = new ArrayList<>();
            for (VendorService vendorService : providedServicesByVendor){
                VendorServiceDetails vendorServiceDetails = new VendorServiceDetails();
                vendorServiceDetails.setDuration(vendorService.getDuration());
                vendorServiceDetails.setPrice(vendorService.getPrice());
                vendorServiceDetails.setServiceCategory(vendorService.getServiceCategory());
                serviceDetails.add(vendorServiceDetails);
            }
            DisplayVendorDetails vendorDetails = new DisplayVendorDetails();
            vendorDetails.setVendorName(vendor.getUser().getName());
            vendorDetails.setVendorEmail(vendor.getUser().getEmail());
            vendorDetails.setVendorServiceDetails(serviceDetails);
            vendorDetailsList.add(vendorDetails);
        }
        return vendorDetailsList;
    }
    public DisplayVendorDetails viewAssignedServices(Long id){
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new VendorNotFoundException("Vendor not found."));
        DisplayVendorDetails displayVendorDetails = new DisplayVendorDetails();
        List<VendorServiceDetails> vendorServiceDetailsList = new ArrayList<>();
        displayVendorDetails.setVendorName(vendor.getUser().getName());
        displayVendorDetails.setVendorEmail(vendor.getUser().getEmail());
        for(VendorService service : vendor.getVendorServices()){
            VendorServiceDetails vendorServiceDetails = new VendorServiceDetails();
            vendorServiceDetails.setPrice(service.getPrice());
            vendorServiceDetails.setDuration(service.getDuration());
            vendorServiceDetails.setServiceCategory(service.getServiceCategory());
            vendorServiceDetailsList.add(vendorServiceDetails);
        }
        displayVendorDetails.setVendorServiceDetails(vendorServiceDetailsList);
        return displayVendorDetails;
    }
    public DisplayVendorDetails displayVendor(Long id) {
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new NoVendorFoundException("Vendor not found"));

        User user = vendor.getUser();
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
        displayVendorDetails.setVendorStatus(vendor.getStatus());
        return displayVendorDetails;
    }
    public VendorUpdateResponse updateVendor(Long vendorId, VendorUpdateRequest request){
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found."));
        vendor.getUser().setName(request.getVendorName());
        vendor.getUser().setEmail(request.getVendorEmail());
        vendor.getUser().setPassword(request.getPassword());
        vendorRepository.save(vendor);
        VendorUpdateResponse response = new VendorUpdateResponse();
        response.setVendorId(vendor.getId());
        response.setVendorName(vendor.getUser().getName());
        response.setVendorEmail(vendor.getUser().getEmail());
        return response;
    }
    public VendorDeletionResponse deleteVendor(Long vendorId){
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found."));
        VendorDeletionResponse response = new VendorDeletionResponse();
        response.setVendorName(vendor.getUser().getName());
        response.setVendorEmail(vendor.getUser().getEmail());
        vendorRepository.delete(vendor);
        return response;
    }
    public void removeAssignedServiceFromVendor(Long vendorId, Long serviceId){
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found."));
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(serviceId).orElseThrow(() -> new ServiceNotFoundException("Service not found."));

        vendorServiceRepository.removeByVendor_idAndServiceCategory_id(vendorId, serviceId);
    }
    public ServiceUpdationResponse updateAssignedServiceDetails(Long vendorId, Long serviceId, ServiceUpdationRequest request){
        if(request==null)
            throw new InvalidInputException("Please provide details to update");
        VendorService vendorService = vendorServiceRepository.findByVendor_idAndServiceCategory_id(vendorId, serviceId);
        if(vendorService == null)
            throw new ServiceNotFoundException("Service not found.");
        if(request.getDuration() != null)
            vendorService.setDuration(request.getDuration());
        if(request.getPrice() != null)
            vendorService.setPrice(request.getPrice());
        vendorServiceRepository.save(vendorService);
        ServiceUpdationResponse response = new ServiceUpdationResponse();
        response.setVendorName(vendorService.getVendor().getUser().getName());
        response.setServiceName(vendorService.getServiceCategory().getServiceName());
        response.setPrice(vendorService.getPrice());
        response.setDuration(vendorService.getDuration());
        return response;
    }
    public List<SearchResponse> searchByVendorOrService(String vendorName, String serviceName){
        List<VendorService> vendorServices = vendorServiceRepository.findAll();
        List<SearchResponse> responses = new ArrayList<>();

        if(vendorName != null)
            vendorServices = vendorServices.stream().filter(v -> v.getVendor().getUser() != null &&
                                                                 v.getVendor().getUser().getName() != null &&
                                                                 v.getVendor().getUser().getName().toLowerCase().contains(vendorName.toLowerCase())).toList();
        if(serviceName != null)
            vendorServices = vendorServices.stream().filter(v -> v.getServiceCategory() != null &&
                                                                 v.getServiceCategory().getServiceName() != null &&
                                                                 v.getServiceCategory().getServiceName().toLowerCase().contains(serviceName.toLowerCase())).toList();
        for(VendorService service : vendorServices){
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setVendorId(service.getVendor().getId());
            searchResponse.setVendorName(service.getVendor().getUser().getName());
            searchResponse.setVendorEmail(service.getVendor().getUser().getEmail());
            searchResponse.setServiceName(service.getServiceCategory().getServiceName());
            searchResponse.setPrice(service.getPrice());
            searchResponse.setDuration(service.getDuration());
            responses.add(searchResponse);
        }
        return responses;
    }
    public SearchResponse getSingleAssignedServiceOfVendor(Long vendorId, Long serviceId){
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found."));

        ServiceCategory service = serviceCategoryRepository.findById(serviceId).orElseThrow(() -> new ServiceNotFoundException("Service not found."));
        VendorService vendorService = vendorServiceRepository.findByVendor_idAndServiceCategory_id(vendor.getId(), service.getId());

        if(vendorService == null)
            throw new ServiceAssignmentNotFoundException("Service assignment not found.");

        SearchResponse response = new SearchResponse();
        response.setVendorId(vendor.getUser().getId());
        response.setVendorName(vendor.getUser().getName());
        response.setVendorEmail(vendor.getUser().getEmail());
        response.setServiceName(service.getServiceName());
        response.setPrice(vendorService.getPrice());
        response.setDuration(vendorService.getDuration());
        return response;
    }
    public VendorSummaryResponse viewDashboardSummary(Long vendorId){
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found."));
        Long count = vendorServiceRepository.countByVendor_id(vendorId);

        VendorSummaryResponse response = new VendorSummaryResponse();
        response.setVendorId(vendor.getId());
        response.setVendorName(vendor.getUser().getName());
        response.setVendorEmail(vendor.getUser().getEmail());
        response.setTotalAssignedServices(count);
        return response;
    }
    public Long countTotalVendors(){
        return vendorRepository.count();
    }
    public Long countTotalServices(){
        return serviceCategoryRepository.count();
    }
    public boolean checkVendorHasService(Long vendorId, Long serviceId){
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found."));
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(serviceId).orElseThrow(() -> new ServiceNotFoundException("Service not found."));

        VendorService vendorService = vendorServiceRepository.findByVendor_idAndServiceCategory_id(vendor.getId(), serviceCategory.getId());
        return vendorService != null;
    }
    public List<ServiceAssignmentResponse> bulkServiceAssignment(Long vendorId, List<BulkServiceAssignmentRequest> requests){
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found."));
        List<ServiceAssignmentResponse> responses = new ArrayList<>();

        for(BulkServiceAssignmentRequest request : requests){
            ServiceCategory service = serviceCategoryRepository.findById(request.getServiceCategoryId())
                                                               .orElseThrow(() -> new ServiceNotFoundException("Service not found."));
            VendorService vendorService = new VendorService();
            vendorService.setVendor(vendor);
            vendorService.setServiceCategory(service);
            vendorService.setPrice(request.getPrice());
            vendorService.setDuration(request.getDuration());

            vendorServiceRepository.save(vendorService);

            ServiceAssignmentResponse response = new ServiceAssignmentResponse();
            response.setVendorId(vendor.getId());
            response.setServiceCategoryName(service.getServiceName());
            response.setPrice(request.getPrice());
            response.setDuration(request.getDuration());
            responses.add(response);
        }
        return responses;
    }
}
