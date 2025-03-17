package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.address.AddressResponse;
import com.datn.beestyle.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends IGenericRepository<Address, Long> {
    @Query("""
            select a from Address a 
            where 
                (:customerId is null or a.customer.id = :customerId) 
            """)
    Page<Address> findByCustomerId(Pageable pageable, @Param("customerId") Long customerId);
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.customer.id = :customerId AND a.id <> :addressId")
    void updateIsDefaultFalseForOtherAddresses(Long customerId, Long addressId);

    boolean existsByCustomerIdAndIsDefaultTrue(Long customerId);

    @Query("""
    select new com.datn.beestyle.dto.address.AddressResponse(
        a.id as id,
        a.addressName as addressName,
        a.cityCode as cityCode,
        a.city as city,
        a.districtCode as districtCode,
        a.district as district,
        a.communeCode as communeCode,
        a.commune as commune
    )
    from Address a
    where (:addressId is null or a.id = :addressId)
""")
    AddressResponse findByAddressId(@Param("addressId") Long addressId);

    @Query("""
            select count(a) from Address a
            where :customerId is null or a.customer.id = :customerId
            """)
    Integer countAddressByCustomer(Long customerId);


}
