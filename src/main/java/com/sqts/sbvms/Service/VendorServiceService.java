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
import com.sqts.sbvms.Security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VendorServiceService {
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final VendorServiceRepository vendorServiceRepository;
    private final PasswordEncoder passwordEncoder;
    public VendorServiceService(VendorRepository vendorRepository,
                                UserRepository userRepository,
                                ServiceCategoryRepository serviceCategoryRepository,
                                VendorServiceRepository vendorServiceRepository,
                                PasswordEncoder passwordEncoder) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.vendorServiceRepository = vendorServiceRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<PendingVendorResponse> getPendingVendors() {

        List<Vendor> vendors =
                vendorRepository.findByStatus(VendorStatus.PENDING_APPROVAL);

        if (vendors.isEmpty())
            throw new NoVendorFoundException("No pending vendor requests found.");

        List<PendingVendorResponse> responses = new ArrayList<>();

        for (Vendor vendor : vendors) {

            VendorService vendorService = vendorServiceRepository
                    .findByVendor_Id(vendor.getId())
                    .orElseThrow(() ->
                            new VendorServiceNotFoundException(
                                    "Vendor service not found."));

            PendingVendorServiceResponse serviceResponse =
                    new PendingVendorServiceResponse();

            Duration duration = vendorService.getDuration();

            serviceResponse.setServiceName(
                    vendorService.getServiceCategory().getServiceName());

            serviceResponse.setPrice(vendorService.getPrice());

            serviceResponse.setEstimatedDuration(
                    String.format(
                            "%02d:%02d",
                            duration.toHours(),
                            duration.toMinutesPart()
                    )
            );

            PendingVendorResponse response = new PendingVendorResponse();

            response.setVendorId(vendor.getId());
            response.setVendorName(vendor.getUser().getName());
            response.setEmail(vendor.getUser().getEmail());
            response.setPhoneNumber(vendor.getPhoneNumber());
            response.setExperienceYears(vendor.getExperienceYears());
            response.setDescription(vendor.getDescription());
            response.setVendorAddress(vendor.getVendorAddress());
            response.setStatus(vendor.getStatus());
            response.setService(serviceResponse);

            responses.add(response);
        }

        return responses;
    }
    public List<DisplayVendorDetails> displayAllVendors() {

        List<DisplayVendorDetails> vendorDetailsList = new ArrayList<>();

        List<Vendor> vendorList =
                vendorRepository.findByStatus(VendorStatus.ACTIVE);

        if (vendorList.isEmpty())
            throw new NoVendorFoundException("No vendor found.");

        for (Vendor vendor : vendorList) {

            List<VendorService> providedServicesByVendor =
                    vendor.getVendorServices();

            List<VendorServiceDetails> serviceDetails =
                    new ArrayList<>();

            for (VendorService vendorService : providedServicesByVendor) {

                VendorServiceDetails vendorServiceDetails =
                        new VendorServiceDetails();

                Duration duration = vendorService.getDuration();

                vendorServiceDetails.setDuration(
                        String.format(
                                "%02d:%02d",
                                duration.toHours(),
                                duration.toMinutesPart()
                        )
                );

                vendorServiceDetails.setPrice(vendorService.getPrice());
                vendorServiceDetails.setServiceCategory(
                        vendorService.getServiceCategory());

                serviceDetails.add(vendorServiceDetails);
            }

            DisplayVendorDetails vendorDetails =
                    new DisplayVendorDetails();

            vendorDetails.setVendorName(vendor.getUser().getName());
            vendorDetails.setVendorEmail(vendor.getUser().getEmail());
            vendorDetails.setVendorStatus(vendor.getStatus());
            vendorDetails.setVendorServiceDetails(serviceDetails);
            vendorDetails.setVendorAddress(vendor.getVendorAddress());

            vendorDetailsList.add(vendorDetails);
        }

        return vendorDetailsList;
    }
    public DisplayVendorDetails viewAssignedServices(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        DisplayVendorDetails displayVendorDetails =
                new DisplayVendorDetails();

        List<VendorServiceDetails> vendorServiceDetailsList =
                new ArrayList<>();

        displayVendorDetails.setVendorName(vendor.getUser().getName());
        displayVendorDetails.setVendorEmail(vendor.getUser().getEmail());

        for (VendorService service : vendor.getVendorServices()) {

            VendorServiceDetails vendorServiceDetails =
                    new VendorServiceDetails();

            Duration duration = service.getDuration();

            vendorServiceDetails.setPrice(service.getPrice());

            vendorServiceDetails.setDuration(
                    String.format(
                            "%02d:%02d",
                            duration.toHours(),
                            duration.toMinutesPart()
                    )
            );

            vendorServiceDetails.setServiceCategory(
                    service.getServiceCategory());

            vendorServiceDetailsList.add(vendorServiceDetails);
        }

        displayVendorDetails.setVendorServiceDetails(vendorServiceDetailsList);
        displayVendorDetails.setVendorStatus(vendor.getStatus());
        displayVendorDetails.setVendorAddress(vendor.getVendorAddress());

        return displayVendorDetails;
    }
    public DisplayVendorDetails displayVendor(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() ->
                        new NoVendorFoundException("Vendor not found."));

        User user = vendor.getUser();

        DisplayVendorDetails displayVendorDetails =
                new DisplayVendorDetails();

        List<VendorService> providedServices =
                vendorServiceRepository.findByVendorId(id);

        List<VendorServiceDetails> serviceDetails =
                new ArrayList<>();

        for (VendorService service : providedServices) {

            VendorServiceDetails vendorServiceDetails =
                    new VendorServiceDetails();

            Duration duration = service.getDuration();

            vendorServiceDetails.setPrice(service.getPrice());

            vendorServiceDetails.setDuration(
                    String.format(
                            "%02d:%02d",
                            duration.toHours(),
                            duration.toMinutesPart()
                    )
            );

            vendorServiceDetails.setServiceCategory(
                    service.getServiceCategory());

            serviceDetails.add(vendorServiceDetails);
        }

        displayVendorDetails.setVendorName(user.getName());
        displayVendorDetails.setVendorEmail(user.getEmail());
        displayVendorDetails.setVendorServiceDetails(serviceDetails);
        displayVendorDetails.setVendorStatus(vendor.getStatus());
        displayVendorDetails.setVendorAddress(vendor.getVendorAddress());

        return displayVendorDetails;
    }
    public VendorVerificationResponse getVendorVerificationDetails(Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        VendorService vendorService = vendorServiceRepository
                .findByVendor_Id(vendorId)
                .orElseThrow(() ->
                        new VendorServiceNotFoundException(
                                "Vendor service not found."));

        PendingVendorServiceResponse serviceResponse =
                new PendingVendorServiceResponse();

        Duration duration = vendorService.getDuration();

        serviceResponse.setServiceName(
                vendorService.getServiceCategory().getServiceName());

        serviceResponse.setPrice(vendorService.getPrice());

        serviceResponse.setEstimatedDuration(
                String.format(
                        "%02d:%02d",
                        duration.toHours(),
                        duration.toMinutesPart()
                )
        );

        VendorVerificationResponse response =
                new VendorVerificationResponse();

        response.setVendorId(vendor.getId());
        response.setVendorName(vendor.getUser().getName());
        response.setEmail(vendor.getUser().getEmail());
        response.setPhoneNumber(vendor.getPhoneNumber());
        response.setExperienceYears(vendor.getExperienceYears());
        response.setDescription(vendor.getDescription());
        response.setVendorAddress(vendor.getVendorAddress());

        response.setAadhaarNumber(vendor.getAadhaarNumber());
        response.setAadhaarFrontImage(vendor.getAadhaarFrontImage());
        response.setAadhaarBackImage(vendor.getAadhaarBackImage());
        response.setVerificationDocument(vendor.getVerificationDocument());

        response.setStatus(vendor.getStatus());

        response.setService(serviceResponse);

        return response;
    }
    public void approveVendor(Long vendorId){

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        if(vendor.getStatus() != VendorStatus.PENDING_APPROVAL)
            throw new InvalidVendorStatusException(
                    "Only pending vendors can be approved.");

        vendor.setStatus(VendorStatus.ACTIVE);

        vendorRepository.save(vendor);
    }
    public void rejectVendor(Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        if (vendor.getStatus() != VendorStatus.PENDING_APPROVAL)
            throw new InvalidVendorStatusException(
                    "Only pending vendors can be rejected.");

        vendorServiceRepository.deleteAllByVendor_Id(vendorId);

        vendorRepository.delete(vendor);

        userRepository.delete(vendor.getUser());
    }
    public VendorUpdateResponse updateVendor(Long vendorId, VendorUpdateRequest request){
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found."));

        vendor.getUser().setName(request.getVendorName().trim());

        if (request.getPassword().trim().length() < 8)
            throw new WeakPasswordException("Provided password is weak.");
        vendor.getUser().setPassword(passwordEncoder.encode(request.getPassword().trim()));

        vendor.setVendorAddress(request.getVendorAddress());

        String phone = request.getPhoneNumber().trim();
        if (!phone.equals(vendor.getPhoneNumber())
                && vendorRepository.existsByPhoneNumber(phone)) {
            throw new VendorAlreadyExistsException("Phone number already registered.");
        }
        vendor.setPhoneNumber(phone);

        vendorRepository.save(vendor);
        VendorUpdateResponse response = new VendorUpdateResponse();
        response.setVendorId(vendor.getId());
        response.setVendorName(vendor.getUser().getName());
        response.setVendorAddress(vendor.getVendorAddress());
        response.setPhoneNumber(vendor.getPhoneNumber());
        return response;
    }
    public VendorDeletionResponse deleteVendor(Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        if (vendor.getStatus() != VendorStatus.ACTIVE)
            throw new InvalidVendorStatusException(
                    "Only active vendors can be deleted.");

        VendorDeletionResponse response = new VendorDeletionResponse();

        response.setVendorName(vendor.getUser().getName());
        response.setVendorEmail(vendor.getUser().getEmail());

        vendor.setStatus(VendorStatus.INACTIVE);

        vendorRepository.save(vendor);

        return response;
    }
    public void activateVendor(Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        if (vendor.getStatus() != VendorStatus.INACTIVE)
            throw new InvalidVendorStatusException(
                    "Only inactive vendors can be activated.");

        vendor.setStatus(VendorStatus.ACTIVE);

        vendorRepository.save(vendor);
    }
    public void removeAssignedServiceFromVendor(Long vendorId, Long serviceId) {

        vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() ->
                        new ServiceNotFoundException("Service not found."));

        VendorService vendorService = vendorServiceRepository
                .findByVendor_idAndServiceCategory_id(vendorId, serviceId);

        if (vendorService == null)
            throw new VendorServiceNotFoundException(
                    "Vendor is not assigned this service.");

        long totalServices = vendorServiceRepository.countByVendor_Id(vendorId);

        if (totalServices == 1)
            throw new LastVendorServiceRemovalException(
                    "A vendor must have at least one active service.");

        vendorServiceRepository.delete(vendorService);
    }
    public ServiceUpdationResponse updateAssignedServiceDetails(
            Long vendorId,
            Long serviceId,
            ServiceUpdationRequest request) {

        if (request == null)
            throw new InvalidInputException("Please provide details to update.");

        VendorService vendorService =
                vendorServiceRepository.findByVendor_idAndServiceCategory_id(
                        vendorId,
                        serviceId);

        if (vendorService == null)
            throw new ServiceNotFoundException("Service not found.");

        if (vendorService.getVendor().getStatus() != VendorStatus.ACTIVE)
            throw new InvalidVendorStatusException("Vendor is not active.");

        if (request.getDuration() != null &&
                !request.getDuration().isBlank()) {

            Duration duration;

            try {

                LocalTime time =
                        LocalTime.parse(request.getDuration().trim());

                duration = Duration.ofHours(time.getHour())
                        .plusMinutes(time.getMinute());

            } catch (DateTimeParseException ex) {

                throw new InvalidDurationException(
                        "Duration must be in HH:mm format (e.g. 02:30).");
            }

            if (duration.isZero())
                throw new InvalidDurationException(
                        "Duration must be greater than zero.");

            vendorService.setDuration(duration);
        }

        if (request.getPrice() != null)
            vendorService.setPrice(request.getPrice());

        vendorServiceRepository.save(vendorService);

        Duration duration = vendorService.getDuration();

        ServiceUpdationResponse response =
                new ServiceUpdationResponse();

        response.setVendorName(
                vendorService.getVendor().getUser().getName());

        response.setServiceName(
                vendorService.getServiceCategory().getServiceName());

        response.setPrice(vendorService.getPrice());

        response.setDuration(
                String.format(
                        "%02d:%02d",
                        duration.toHours(),
                        duration.toMinutesPart()
                )
        );

        return response;
    }
    public List<SearchResponse> searchVendorServicesByVendorOrService(
            String vendorName,
            String serviceName) {

        List<VendorService> vendorServices = vendorServiceRepository.findAll();
        List<SearchResponse> responses = new ArrayList<>();

        vendorServices = vendorServices.stream()
                .filter(v -> v.getVendor().getStatus() == VendorStatus.ACTIVE)
                .toList();

        if (vendorName != null && !vendorName.isBlank()) {
            vendorServices = vendorServices.stream()
                    .filter(v ->
                            v.getVendor().getUser() != null
                                    && v.getVendor().getUser().getName() != null
                                    && v.getVendor().getUser().getName()
                                    .trim()
                                    .toLowerCase()
                                    .contains(vendorName.trim().toLowerCase()))
                    .toList();
        }

        if (serviceName != null && !serviceName.isBlank()) {
            vendorServices = vendorServices.stream()
                    .filter(v ->
                            v.getServiceCategory() != null
                                    && v.getServiceCategory().getServiceName() != null
                                    && v.getServiceCategory().getServiceName()
                                    .trim()
                                    .toLowerCase()
                                    .contains(serviceName.trim().toLowerCase()))
                    .toList();
        }

        for (VendorService service : vendorServices) {

            Duration duration = service.getDuration();

            SearchResponse searchResponse = new SearchResponse();

            searchResponse.setVendorId(service.getVendor().getId());
            searchResponse.setVendorName(service.getVendor().getUser().getName());
            searchResponse.setVendorEmail(service.getVendor().getUser().getEmail());
            searchResponse.setServiceName(service.getServiceCategory().getServiceName());
            searchResponse.setPrice(service.getPrice());

            searchResponse.setDuration(
                    String.format(
                            "%02d:%02d",
                            duration.toHours(),
                            duration.toMinutesPart()
                    )
            );

            searchResponse.setVendorAddress(service.getVendor().getVendorAddress());

            responses.add(searchResponse);
        }

        return responses;
    }
    public SearchResponse getSingleAssignedServiceOfVendor(
            Long vendorId,
            Long serviceId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        ServiceCategory service = serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() ->
                        new ServiceNotFoundException("Service not found."));

        VendorService vendorService =
                vendorServiceRepository.findByVendor_idAndServiceCategory_id(
                        vendor.getId(),
                        service.getId());

        if (vendorService == null)
            throw new ServiceAssignmentNotFoundException(
                    "Service assignment not found.");

        Duration duration = vendorService.getDuration();

        SearchResponse response = new SearchResponse();

        response.setVendorId(vendor.getId());
        response.setVendorName(vendor.getUser().getName());
        response.setVendorEmail(vendor.getUser().getEmail());
        response.setServiceName(service.getServiceName());
        response.setPrice(vendorService.getPrice());

        response.setDuration(
                String.format(
                        "%02d:%02d",
                        duration.toHours(),
                        duration.toMinutesPart()
                )
        );

        response.setVendorAddress(vendor.getVendorAddress());

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
        response.setStatus(vendor.getStatus());
        response.setVendorAddress(vendor.getVendorAddress());
        return response;
    }
    public Long countTotalVendors(){
        return vendorRepository.countByStatus(VendorStatus.ACTIVE);
    }
    public VendorProfileResponse getMyProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Vendor vendor = vendorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        VendorProfileResponse response = new VendorProfileResponse();

        response.setVendorId(vendor.getId());
        response.setName(vendor.getUser().getName());
        response.setEmail(vendor.getUser().getEmail());
        response.setPhoneNumber(vendor.getPhoneNumber());
        response.setVendorAddress(vendor.getVendorAddress());
        response.setExperienceYears(vendor.getExperienceYears());
        response.setDescription(vendor.getDescription());
        response.setStatus(vendor.getStatus());

        return response;
    }
    public VendorUpdateResponse updateMyProfile(VendorUpdateRequest request){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Vendor vendor = vendorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        vendor.getUser().setName(request.getVendorName().trim());

        if(request.getPassword().trim().length() < 8)
            throw new WeakPasswordException("Provided password is weak.");
        vendor.getUser().setPassword(
                passwordEncoder.encode(request.getPassword().trim()));

        vendor.setVendorAddress(request.getVendorAddress());

        String phone = request.getPhoneNumber().trim();
        if(!phone.equals(vendor.getPhoneNumber())
                && vendorRepository.existsByPhoneNumber(phone))
            throw new VendorAlreadyExistsException(
                    "Phone number already registered.");

        vendor.setPhoneNumber(phone);

        vendorRepository.save(vendor);

        VendorUpdateResponse response = new VendorUpdateResponse();

        response.setVendorId(vendor.getId());
        response.setVendorName(vendor.getUser().getName());
        response.setVendorAddress(vendor.getVendorAddress());
        response.setPhoneNumber(vendor.getPhoneNumber());

        return response;
    }
    public VendorSummaryResponse getMyDashboard() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Vendor vendor = vendorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        Long count = vendorServiceRepository.countByVendor_id(vendor.getId());

        VendorSummaryResponse response = new VendorSummaryResponse();

        response.setVendorId(vendor.getId());
        response.setVendorName(vendor.getUser().getName());
        response.setVendorEmail(vendor.getUser().getEmail());
        response.setTotalAssignedServices(count);
        response.setStatus(vendor.getStatus());
        response.setVendorAddress(vendor.getVendorAddress());

        return response;
    }
    public DisplayVendorDetails getMyServices() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Vendor vendor = vendorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        DisplayVendorDetails response = new DisplayVendorDetails();

        response.setVendorName(vendor.getUser().getName());
        response.setVendorEmail(vendor.getUser().getEmail());
        response.setVendorStatus(vendor.getStatus());
        response.setVendorAddress(vendor.getVendorAddress());

        List<VendorServiceDetails> serviceDetails = new ArrayList<>();

        for (VendorService service : vendor.getVendorServices()) {

            VendorServiceDetails details = new VendorServiceDetails();

            Duration duration = service.getDuration();

            details.setServiceCategory(service.getServiceCategory());
            details.setPrice(service.getPrice());

            details.setDuration(
                    String.format(
                            "%02d:%02d",
                            duration.toHours(),
                            duration.toMinutesPart()
                    )
            );

            serviceDetails.add(details);
        }

        response.setVendorServiceDetails(serviceDetails);

        return response;
    }
    public SearchResponse getMyService(Long serviceId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Vendor vendor = vendorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        ServiceCategory serviceCategory = serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() ->
                        new ServiceNotFoundException("Service not found."));

        VendorService vendorService =
                vendorServiceRepository.findByVendor_idAndServiceCategory_id(
                        vendor.getId(),
                        serviceCategory.getId());

        if (vendorService == null)
            throw new ServiceAssignmentNotFoundException(
                    "Service assignment not found.");

        Duration duration = vendorService.getDuration();

        SearchResponse response = new SearchResponse();

        response.setVendorId(vendor.getId());
        response.setVendorName(vendor.getUser().getName());
        response.setVendorEmail(vendor.getUser().getEmail());
        response.setServiceName(serviceCategory.getServiceName());
        response.setPrice(vendorService.getPrice());

        response.setDuration(
                String.format(
                        "%02d:%02d",
                        duration.toHours(),
                        duration.toMinutesPart()
                )
        );

        response.setVendorAddress(vendor.getVendorAddress());

        return response;
    }
    public void removeMyService(Long serviceId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Vendor vendor = vendorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() ->
                        new VendorNotFoundException("Vendor not found."));

        ServiceCategory serviceCategory = serviceCategoryRepository
                .findById(serviceId)
                .orElseThrow(() ->
                        new ServiceNotFoundException("Service not found."));

        VendorService vendorService =
                vendorServiceRepository.findByVendor_idAndServiceCategory_id(
                        vendor.getId(),
                        serviceCategory.getId());

        if (vendorService == null)
            throw new ServiceAssignmentNotFoundException(
                    "Service assignment not found.");

        long totalServices =
                vendorServiceRepository.countByVendor_Id(vendor.getId());

        if (totalServices == 1)
            throw new LastVendorServiceRemovalException(
                    "A vendor must have at least one active service.");

        vendorServiceRepository.delete(vendorService);
    }
}
