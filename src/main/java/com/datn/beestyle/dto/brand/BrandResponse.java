package com.datn.beestyle.dto.brand;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrandResponse{
    Integer id;
    String brandName;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public BrandResponse(Integer id, String brandName) {
        this.id = id;
        this.brandName = brandName;
    }
}
