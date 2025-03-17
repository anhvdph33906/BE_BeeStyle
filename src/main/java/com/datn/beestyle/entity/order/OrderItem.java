package com.datn.beestyle.entity.order;

import com.datn.beestyle.entity.product.ProductVariant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Table(name = "order_item")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "quantity")
    int quantity;

    @Column(name = "sale_price")
    BigDecimal salePrice = BigDecimal.ZERO;

    @Column(name = "discounted_price")
    BigDecimal discountedPrice = BigDecimal.ZERO;

    @Column(name = "note")
    String note;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", referencedColumnName = "id")
    ProductVariant productVariant;
}
