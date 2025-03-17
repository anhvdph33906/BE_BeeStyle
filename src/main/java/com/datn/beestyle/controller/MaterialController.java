package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.material.CreateMaterialRequest;
import com.datn.beestyle.dto.material.UpdateMaterialRequest;
import com.datn.beestyle.service.material.IMaterialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@Validated
@RestController
@RequestMapping("/admin/material")
@RequiredArgsConstructor
@Tag(name = "Material Controller")
public class MaterialController {

    private final IMaterialService materialService;

    @GetMapping
    public ApiResponse<?> getMaterials(Pageable pageable,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String status) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Materials",
                materialService.getAllByNameAndStatus(pageable, name, status));
    }

    @GetMapping("/material-options")
    public ApiResponse<?> getOptionMaterials() {
        return new ApiResponse<>(HttpStatus.OK.value(), "Material options", materialService.getAllByStatusIsActive());
    }


    @PostMapping("/create")
    public ApiResponse<?> createMaterial(@Valid @RequestBody CreateMaterialRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm mới chất liệu thành công!",
                materialService.create(request));
    }

    @PostMapping("/creates")
    public ApiResponse<?> createMaterials(@RequestBody List<@Valid CreateMaterialRequest> requestList) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm mới chất liệu thành công!",
                materialService.createEntities(requestList));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateMaterial(@Min(1) @PathVariable int id,
                                         @Valid @RequestBody UpdateMaterialRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Cập nhật chất liệu thành công!",
                materialService.update(id, request));
    }

    @PatchMapping("/updates")
    public ApiResponse<?> updateMaterials(@Valid @RequestBody List<UpdateMaterialRequest> requestList) {
        materialService.updateEntities(requestList);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Cập nhật chất liệu thành công!");
    }

//    @DeleteMapping("/delete/{id}")
//    public ApiResponse<?> deleteMaterial(@Min(1) @PathVariable int id) {
//        materialService.delete(id);
//        return new ApiResponse<>(HttpStatus.OK.value(), "Material deleted successfully.");
//    }

    @GetMapping("/{id}")
    public ApiResponse<?> getMaterial(@Min(1) @PathVariable int id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Material", materialService.getDtoById(id));
    }
}
