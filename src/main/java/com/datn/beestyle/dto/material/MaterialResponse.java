package com.datn.beestyle.dto.material;

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
public class MaterialResponse{
    Integer id;
    String materialName;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public MaterialResponse(Integer id, String materialName) {
        this.id = id;
        this.materialName = materialName;
    }
}
