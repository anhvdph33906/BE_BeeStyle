package com.datn.beestyle.config;

import com.datn.beestyle.entity.user.Customer;
import com.datn.beestyle.entity.user.Staff;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditAwareImpl<T> implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object object = (UserDetails) authentication.getPrincipal();
        if (object instanceof Staff) {
            return Optional.of(((Staff) object).getId());
        } else {
            return Optional.of(((Customer) object).getId());
        }
    }
}
