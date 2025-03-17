package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.category.CreateCategoryRequest;
import com.datn.beestyle.dto.category.UpdateCategoryRequest;
import com.datn.beestyle.repository.CategoryRepository;
import com.datn.beestyle.service.category.ICategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
@Tag(name = "Category Controller")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping
    public ApiResponse<?> getCategories(Pageable pageable,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) String status) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Categories",
                categoryService.getAllForAdmin(pageable, keyword, status));

    }

    @GetMapping("/category-options")
    public ApiResponse<?> getCategoryOptions() {
        return new ApiResponse<>(HttpStatus.OK.value(), "Category options", categoryService.getCategoryOptions());
    }

    @PostMapping("/create")
    public ApiResponse<?> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm mới danh mục thành công!",
                categoryService.create(request));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateCategory(@Min(1) @PathVariable int id,
                                         @Valid @RequestBody UpdateCategoryRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Cập nhật danh mục thành công!",
                categoryService.update(id, request));
    }


    @GetMapping("/{id}")
    public ApiResponse<?> getCategory(@Min(1) @PathVariable int id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Category", categoryService.getDtoById(id));
    }
}
