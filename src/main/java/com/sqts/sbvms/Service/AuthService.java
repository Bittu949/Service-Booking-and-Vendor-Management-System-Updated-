package com.sqts.sbvms.Service;

import com.sqts.sbvms.Dto.RegisterRequest;
import com.sqts.sbvms.Entity.User;
import com.sqts.sbvms.Enum.Role;
import com.sqts.sbvms.Exception.InvalidInputException;
import com.sqts.sbvms.Exception.UserAlreadyExistsException;
import com.sqts.sbvms.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository =  userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
