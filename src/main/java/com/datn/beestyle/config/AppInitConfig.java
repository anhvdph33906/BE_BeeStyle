package com.datn.beestyle.config;

import com.datn.beestyle.entity.user.Staff;
import com.datn.beestyle.enums.Role;
import com.datn.beestyle.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppInitConfig {

    private final StaffRepository staffRepository;

    @Bean
    ApplicationRunner applicationRunner() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return args -> {
            if (staffRepository.findByUsername("admin").isEmpty()) {
                Staff staff = new Staff();
                staff.setUsername("admin");
                staff.setPassword(passwordEncoder.encode("admin12345"));
                staff.setFullName("Administrator");
                staff.setGender(1);
                staff.setPhoneNumber("0912345678");
                staff.setDateOfBirth(LocalDate.now());
                staff.setRole(Role.ADMIN);
                staff.setStatus(1);

                staffRepository.save(staff);
                log.warn("Admin user has been created with default password: admin, please change it.");
            }
        };
    }
}
