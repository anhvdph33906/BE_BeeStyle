package com.datn.beestyle.dto.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCategoryResponse {

    Integer id;

    String categoryName;

    String slug;

    List<UserCategoryResponse> categoryChildren = new ArrayList<>();

    public UserCategoryResponse(Integer id, String categoryName, String slug) {
        this.id = id;
        this.categoryName = categoryName;
        this.slug = slug;
    }
}
