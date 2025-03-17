package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.brand.CreateBrandRequest;
import com.datn.beestyle.dto.brand.UpdateBrandRequest;
import com.datn.beestyle.service.brand.IBrandService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/admin/brand")
@RequiredArgsConstructor
@Tag(name = "Brand Controller")
public class BrandController {

    private final IBrandService brandService;

    @Operation(method = "GET", summary = "Get list of brands",
            description = "Send a request via this API to get brand list and search with paging by name and status")
    @GetMapping
    public ApiResponse<?> getBrands(Pageable pageable,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String status) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Brands",
                brandService.getAllByNameAndStatus(pageable, name, status));
    }

    @GetMapping("/brand-options")
    public ApiResponse<?> getOptionBrands() {
        return new ApiResponse<>(HttpStatus.OK.value(), "Brand options", brandService.getAllByStatusIsActive());
    }


    @PostMapping("/create")
    public ApiResponse<?> createBrand(@Valid @RequestBody CreateBrandRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm mới thương hiệu thành công!",
                brandService.create(request));
    }

    @PostMapping("/creates")
    public ApiResponse<?> createBrands(@RequestBody List<@Valid CreateBrandRequest> requestList) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Brands added successfully",
                brandService.createEntities(requestList));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateBrand(@Min(1) @PathVariable int id,
                                      @Valid @RequestBody UpdateBrandRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Cập nhật thương hiệu thành công!",
                brandService.update(id, request));
    }

    @PatchMapping("/updates")
    public ApiResponse<?> updateBrands(@RequestBody List<@Valid UpdateBrandRequest> requestList) {
        brandService.updateEntities(requestList);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Brands updated successfully");
    }

//    @Operation(summary = "Delete user permanently", description = "Send a request via this API to delete user permanently")
//    @DeleteMapping("/delete/{id}")
//    public ApiResponse<?> deleteBrand(@Min(1) @PathVariable int id) {
//        brandService.delete(id);
//        return new ApiResponse<>(HttpStatus.OK.value(), "Brand deleted successfully.");
//    }

    @GetMapping("/{id}")
    public ApiResponse<?> getBrand(@Min(1) @PathVariable int id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Brand", brandService.getDtoById(id));
    }
}
