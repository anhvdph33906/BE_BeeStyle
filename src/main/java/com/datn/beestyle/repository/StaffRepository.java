package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.entity.user.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StaffRepository extends IGenericRepository<Staff,Integer> {
    @Query("""
            select s from Staff s 
            where 
                (:keyword is null or 
                    s.fullName like concat('%', :keyword, '%') or 
                    s.phoneNumber like concat('%', :keyword, '%') or
                    s.email like concat('%', :keyword,'%')) and
                (:status is null or s.status = :status) and
                (:gender is null or s.gender = :gender)
            """)
    Page<Staff> findByKeywordContainingAndStatusAndGender(Pageable pageable, @Param("status") Integer status,
                                                          @Param("gender") Integer gender, @Param("keyword") String keyword);


    boolean existsByEmail(String email);
    boolean existsByUsername(String userName);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Staff> findByUsername(String username);

}
