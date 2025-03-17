package com.datn.beestyle.dto.product.attributes.color;

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
public class ColorResponse{
    Integer id;
    String colorCode;
    String colorName;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public ColorResponse(Integer id, String colorCode, String colorName) {
        this.id = id;
        this.colorCode = colorCode;
        this.colorName = colorName;
    }
}
