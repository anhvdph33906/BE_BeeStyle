package com.datn.beestyle.controller.user;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.service.category.ICategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name = "User Category Controller")
public class UserCategoryController {
    private final ICategoryService categoryService;
    @GetMapping
    public ApiResponse<?> getCategoriesForUser(Pageable pageable) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Categories", categoryService.getCategoryOptions());
    }
}
