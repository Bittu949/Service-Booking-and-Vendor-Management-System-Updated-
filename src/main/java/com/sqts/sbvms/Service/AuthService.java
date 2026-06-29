package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.LoginRequest;
import com.sqts.sbvms.Dto.RegisterRequest;
import com.sqts.sbvms.Entity.User;
import com.sqts.sbvms.Entity.Vendor;
import com.sqts.sbvms.Enum.Role;
import com.sqts.sbvms.Enum.VendorStatus;
import com.sqts.sbvms.Exception.*;
import com.sqts.sbvms.Repository.UserRepository;
import com.sqts.sbvms.Repository.VendorRepository;
import com.sqts.sbvms.Security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final VendorRepository vendorRepository;
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       VendorRepository vendorRepository){
        this.userRepository =  userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.vendorRepository = vendorRepository;
    }
    public String register(RegisterRequest request){
        User user = userRepository.findByEmail(request.getEmail());
        if(user != null)
            throw new UserAlreadyExistsException("User already exists.");

        if(!request.getPassword().equals(request.getConfirmPassword()))
            throw new InvalidInputException("Password not matching.");

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.CUSTOMER);

        userRepository.save(newUser);
        return "User registered successfully.";
    }
    public String login(LoginRequest request){
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword());
        Authentication authenticatedUser = authenticationManager.authenticate(authentication);
        UserDetails userDetails = ((UserDetails) authenticatedUser.getPrincipal());

        User user = userRepository.findByEmail(userDetails.getUsername());
        if(user.getRole() == Role.VENDOR) {
            Vendor vendor = vendorRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new VendorNotFoundException("Vendor not found."));

            if (vendor.getStatus() == VendorStatus.PENDING_APPROVAL)
                throw new VendorApprovalPendingException(
                        "Your registration request is pending admin approval.");

            if (vendor.getStatus() == VendorStatus.REJECTED)
                throw new VendorRegistrationRejectedException(
                        "Your registration request has been rejected by the administrator.");

            if (vendor.getStatus() == VendorStatus.INACTIVE)
                throw new VendorAccountInactiveException(
                        "Your account has been deactivated.");
        }

        return jwtService.generateToken(userDetails);
    }
}
