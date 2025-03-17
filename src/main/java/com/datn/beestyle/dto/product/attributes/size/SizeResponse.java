package com.datn.beestyle.dto.product.attributes.size;

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
public class SizeResponse{
    Integer id;
    String sizeName;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public SizeResponse(Integer id, String sizeName) {
        this.id = id;
        this.sizeName = sizeName;
    }
}
