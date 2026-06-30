package com.sqts.sbvms.Config;

import com.sqts.sbvms.Entity.User;
import com.sqts.sbvms.Enum.Role;
import com.sqts.sbvms.Repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartupConfig {
    @Bean
    public ApplicationRunner adminInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder){

        return   args -> {
                if(userRepository.existsByRole(Role.ADMIN))
                    return;
            User user = new User();
            user.setName("Admin");
            user.setEmail("admin123@gmail.com");
            user.setPassword(passwordEncoder.encode("admin@123"));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
            };
    }
}
