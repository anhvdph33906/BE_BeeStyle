package com.datn.beestyle.entity;

import com.datn.beestyle.entity.user.Customer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static jakarta.persistence.CascadeType.ALL;

@Table(name = "address")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Address extends BaseEntity<Long> {

    @Column(name = "address_name")
    String addressName;

    @Column(name = "city_code")
    Integer cityCode;

    @Column(name = "city")
    String city;

    @Column(name = "district_code")
    Integer districtCode;

    @Column(name = "district")
    String district;

    @Column(name = "commune_code")
    Integer communeCode;

    @Column(name = "commune")
    String commune;

    @Column(name = "is_default")
    Boolean isDefault;

    @ManyToOne(cascade = {ALL})
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    Customer customer;
}
