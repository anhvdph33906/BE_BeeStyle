package com.datn.beestyle.service.product.attributes.size;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.product.attributes.size.CreateSizeRequest;
import com.datn.beestyle.dto.product.attributes.size.SizeResponse;
import com.datn.beestyle.dto.product.attributes.size.UpdateSizeRequest;
import com.datn.beestyle.entity.product.attributes.Size;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISizeService
        extends IGenericService<Size, Integer, CreateSizeRequest, UpdateSizeRequest, SizeResponse> {

    PageResponse<?> getAllByNameAndStatus(Pageable pageable, String name, String status);

    List<SizeResponse> getAllByStatusIsActive();
}
