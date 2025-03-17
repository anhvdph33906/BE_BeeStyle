package com.datn.beestyle.service.brand;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.brand.BrandResponse;
import com.datn.beestyle.dto.brand.CreateBrandRequest;
import com.datn.beestyle.dto.brand.UpdateBrandRequest;
import com.datn.beestyle.dto.material.MaterialResponse;
import com.datn.beestyle.entity.product.attributes.Brand;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBrandService
        extends IGenericService<Brand, Integer, CreateBrandRequest, UpdateBrandRequest, BrandResponse> {

    PageResponse<?> getAllByNameAndStatus(Pageable pageable, String name, String status);

    List<BrandResponse> getAllByStatusIsActive();
}
