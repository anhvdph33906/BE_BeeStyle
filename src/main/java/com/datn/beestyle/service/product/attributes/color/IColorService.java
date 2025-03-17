package com.datn.beestyle.service.product.attributes.color;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.product.attributes.color.ColorResponse;
import com.datn.beestyle.dto.product.attributes.color.CreateColorRequest;
import com.datn.beestyle.dto.product.attributes.color.UpdateColorRequest;
import com.datn.beestyle.dto.product.attributes.size.SizeResponse;
import com.datn.beestyle.entity.product.attributes.Color;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IColorService
        extends IGenericService<Color, Integer, CreateColorRequest, UpdateColorRequest, ColorResponse> {

    PageResponse<?> getAllByNameAndStatus(Pageable pageable, String name, String status);

    List<ColorResponse> getAllByStatusIsActive();
}
