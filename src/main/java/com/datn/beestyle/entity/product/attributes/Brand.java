package com.datn.beestyle.entity.product.attributes;

import com.datn.beestyle.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "brand")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Brand extends BaseEntity<Integer> {

    @Column(name = "brand_name")
    String brandName;

    @Column(name = "status")
    int status;

    public Brand(Integer id, String brandName) {
        super(id);
        this.brandName = brandName;
    }
}
