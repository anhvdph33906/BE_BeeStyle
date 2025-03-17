package com.datn.beestyle.entity.product;

import com.datn.beestyle.dto.product.variant.ProductVariantResponse;
import com.datn.beestyle.dto.statistics.InventoryResponse;
import com.datn.beestyle.entity.Auditable;
import com.datn.beestyle.entity.Promotion;
import com.datn.beestyle.entity.product.attributes.Color;
import com.datn.beestyle.entity.product.attributes.Size;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

import static jakarta.persistence.CascadeType.*;

@Table(name = "product_variant")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SqlResultSetMapping(
        name = "ProductVariantResponseMappingByStock",
        classes = @ConstructorResult(
                targetClass = InventoryResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "sku", type = String.class),
                        @ColumnResult(name = "productId", type = Long.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "colorId", type = Integer.class),
                        @ColumnResult(name = "colorCode", type = String.class),
                        @ColumnResult(name = "colorName", type = String.class),
                        @ColumnResult(name = "sizeId", type = Integer.class),
                        @ColumnResult(name = "sizeName", type = String.class),
                        @ColumnResult(name = "quantityInStock", type = Integer.class),
                        @ColumnResult(name = "imageUrl", type = String.class)
                }
        )
)
@SqlResultSetMapping(
        name = "ProductVariantResponseMappingByQuantitySold",
        classes = @ConstructorResult(
                targetClass = InventoryResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "productId", type = Long.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "sku", type = String.class),
                        @ColumnResult(name = "colorId", type = Integer.class),
                        @ColumnResult(name = "colorCode", type = String.class),
                        @ColumnResult(name = "colorName", type = String.class),
                        @ColumnResult(name = "sizeId", type = Integer.class),
                        @ColumnResult(name = "sizeName", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                        @ColumnResult(name = "totalQuantitySold", type = Integer.class)
                }
        )
)

public class ProductVariant extends Auditable<Long> {

    @Column(name = "sku")
    String sku;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    Size size;

    @Column(name = "original_price")
    BigDecimal originalPrice = BigDecimal.ZERO;

    @Column(name = "sale_price")
    BigDecimal salePrice = BigDecimal.ZERO;

    @Column(name = "quantity_in_stock")
    int quantityInStock;

    @Column(name = "status")
    int status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {PERSIST, MERGE})
    @JoinColumn(name = "promotion_id", referencedColumnName = "id")
    Promotion promotion;
}
