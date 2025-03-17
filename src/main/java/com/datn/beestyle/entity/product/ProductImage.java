package com.datn.beestyle.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Table(name = "product_image")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "is_default")
    Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
}
