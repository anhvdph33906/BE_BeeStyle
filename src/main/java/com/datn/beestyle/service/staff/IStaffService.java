package com.datn.beestyle.service.staff;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.staff.CreateStaffRequest;
import com.datn.beestyle.dto.staff.StaffResponse;
import com.datn.beestyle.dto.staff.UpdateStaffRequest;
import com.datn.beestyle.entity.user.Staff;
import org.springframework.data.domain.Pageable;

public interface IStaffService
    extends IGenericService<Staff,Integer, CreateStaffRequest, UpdateStaffRequest, StaffResponse> {
    PageResponse<?> getAllByKeywordAndStatusAndGender(Pageable pageable,String status,String gender,String keyword);

    Staff getStaffByUsername(String username);
}
