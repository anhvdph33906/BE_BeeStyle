package com.datn.beestyle.entity.user;

import com.datn.beestyle.dto.customer.CustomerResponse;
import com.datn.beestyle.dto.statistics.RevenueStatisticsResponse;
import com.datn.beestyle.entity.Address;
import com.datn.beestyle.entity.BaseEntity;
import com.datn.beestyle.enums.Role;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;

@Table(name = "customer")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@SqlResultSetMapping(
        name = "ProductSalesByUserMapping",
        classes = @ConstructorResult(
                targetClass = CustomerResponse.class,
                columns = {
                        @ColumnResult(name = "product_name", type = String.class),
                        @ColumnResult(name = "sale_price", type = BigDecimal.class),
                        @ColumnResult(name = "total_quantity", type = Integer.class),
                        @ColumnResult(name = "image_product", type = String.class)
                }
        )
)
public class Customer extends BaseEntity<Long> implements UserDetails {

    @Column(name = "full_name")
    String fullName;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    LocalDate dateOfBirth;

    @Column(name = "gender")
    int gender;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    Role role;

    @Column(name = "status")
    int status;

//    @OneToOne(fetch = FetchType.LAZY, cascade = ALL)
//    ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "customer", cascade = ALL, fetch = FetchType.LAZY)
    Set<Address> addresses = new HashSet<>();

    public void addAddress(Address address) {
        if (address != null) {
            if (addresses == null) {
                addresses = new HashSet<>();
            }
            addresses.add(address);
            address.setCustomer(this); // save customer id
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("OWNER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Customer(Long id, String fullName, LocalDate dateOfBirth, int gender, String email, Role role) {
        super(id);
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.role = role;
    }
}
