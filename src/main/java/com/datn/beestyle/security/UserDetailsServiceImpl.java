package com.datn.beestyle.security;

import com.datn.beestyle.entity.user.Customer;
import com.datn.beestyle.entity.user.Staff;
import com.datn.beestyle.repository.customer.CustomerRepository;
import com.datn.beestyle.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Staff> staffOpt = staffRepository.findByUsername(username);
        if (staffOpt.isPresent()) {
            return staffOpt.get();
        }

        Optional<Customer> customerOpt = customerRepository.findByEmail(username);
        if (customerOpt.isPresent()) {
            return customerOpt.get();
        }

        throw new UsernameNotFoundException("Tài khoản: " + username + " không tồn tại");
    }
}
