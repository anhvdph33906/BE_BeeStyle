package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.product.attributes.color.CreateColorRequest;
import com.datn.beestyle.dto.product.attributes.color.UpdateColorRequest;
import com.datn.beestyle.service.product.attributes.color.IColorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/admin/color")
@RequiredArgsConstructor
@Tag(name = "Color Controller")
public class ColorController {
    
    private final IColorService colorService;

    @GetMapping
    public ApiResponse<?> getColors(Pageable pageable,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String status) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Colors",
                colorService.getAllByNameAndStatus(pageable, name, status));
    }

    @GetMapping("/color-options")
    public ApiResponse<?> getOptionBrands() {
        return new ApiResponse<>(HttpStatus.OK.value(), "Color options", colorService.getAllByStatusIsActive());
    }

    @PostMapping("/create")
    public ApiResponse<?> createColor(@Valid @RequestBody CreateColorRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm mới màu sắc thành công!",
                colorService.create(request));
    }

    @PostMapping("/creates")
    public ApiResponse<?> createColors(@RequestBody List<@Valid CreateColorRequest> requestList) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm mới màu sắc thành công!",
                colorService.createEntities(requestList));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateColor(@Min(1) @PathVariable int id, @Valid @RequestBody UpdateColorRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Cập nhật màu sắc thành công!",
                colorService.update(id, request));
    }

    @PatchMapping("/updates")
    public ApiResponse<?> updateColors(@RequestBody List<@Valid UpdateColorRequest> requestList) {
        colorService.updateEntities(requestList);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Cập nhật màu sắc thành công!");
    }

//    @DeleteMapping("/delete/{id}")
//    public ApiResponse<?> deleteColor(@Min(1) @PathVariable int id) {
//        colorService.delete(id);
//        return new ApiResponse<>(HttpStatus.OK.value(), "Color deleted successfully.");
//    }

    @GetMapping("/{id}")
    public ApiResponse<?> getColor(@Min(1) @PathVariable int id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Color", colorService.getDtoById(id));
    }
}
