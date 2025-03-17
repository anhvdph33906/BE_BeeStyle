package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.product.attributes.size.CreateSizeRequest;
import com.datn.beestyle.dto.product.attributes.size.UpdateSizeRequest;
import com.datn.beestyle.service.product.attributes.size.ISizeService;
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
@RequestMapping("/admin/size")
@RequiredArgsConstructor
@Tag(name = "Size Controller")
public class SizeController {
    
    private final ISizeService sizeService;
    
    @GetMapping
    public ApiResponse<?> getSizes(Pageable pageable,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String status) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Sizes",
                sizeService.getAllByNameAndStatus(pageable, name, status));
    }

    @GetMapping("/size-options")
    public ApiResponse<?> getOptionBrands() {
        return new ApiResponse<>(HttpStatus.OK.value(), "Size options", sizeService.getAllByStatusIsActive());
    }

    @PostMapping("/create")
    public ApiResponse<?> createSize(@Valid @RequestBody CreateSizeRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm mới size thành công!",
                sizeService.create(request));
    }

    @PostMapping("/creates")
    public ApiResponse<?> createSizes(@RequestBody List<@Valid CreateSizeRequest> requestList) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm mới size thành công!",
                sizeService.createEntities(requestList));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateSize(@Min(1) @PathVariable int id, @Valid @RequestBody UpdateSizeRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Cập nhật size thành công!",
                sizeService.update(id, request));
    }

    @PatchMapping("/updates")
    public ApiResponse<?> updateSizes(@RequestBody List<@Valid UpdateSizeRequest> requestList) {
        sizeService.updateEntities(requestList);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Cập nhật size thành công!");
    }

//    @DeleteMapping("/delete/{id}")
//    public ApiResponse<?> deleteSize(@Min(1) @PathVariable int id) {
//        sizeService.delete(id);
//        return new ApiResponse<>(HttpStatus.OK.value(), "Size deleted successfully.");
//    }

    @GetMapping("/{id}")
    public ApiResponse<?> getSize(@Min(1) @PathVariable int id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Size", sizeService.getDtoById(id));
    }
}
