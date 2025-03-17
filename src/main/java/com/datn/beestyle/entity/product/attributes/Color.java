package com.datn.beestyle.entity.product.attributes;

import com.datn.beestyle.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Table(name = "color")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Color extends BaseEntity<Integer> {

    @Column(name = "color_code")
    String colorCode;

    @Column(name = "color_name")
    String colorName;

    @Column(name = "status")
    int status;
}
