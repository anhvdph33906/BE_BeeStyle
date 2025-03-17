package com.datn.beestyle.repository.customer;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.entity.user.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;


public interface CustomerRepository extends IGenericRepository<Customer, Long> {
    @Query("""
            select c from Customer c 
            where 
                (:keyword is null or 
                    c.fullName like concat('%', :keyword, '%') or 
                    c.phoneNumber like concat('%', :keyword, '%') or
                    c.email like concat('%', :keyword,'%')) and
                (:status is null or c.status = :status) and
                (:gender is null or c.gender = :gender)
            """)
    Page<Customer> findByKeywordContainingAndStatusAndGender(Pageable pageable,
                                                             @Param("status") Integer status,
                                                             @Param("gender") Integer gender,
                                                             @Param("keyword") String keyword);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);


    Optional<Customer> findByEmail(String email);

}


