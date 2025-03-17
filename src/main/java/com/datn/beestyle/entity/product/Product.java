package com.datn.beestyle.entity.product;

import com.datn.beestyle.dto.product.ProductResponse;
import com.datn.beestyle.entity.Auditable;
import com.datn.beestyle.entity.Category;
import com.datn.beestyle.entity.product.attributes.Brand;
import com.datn.beestyle.entity.product.attributes.Material;
import com.datn.beestyle.util.AppUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;


@Table(name = "product")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SqlResultSetMapping(
        name = "ProductResponseMapping",
        classes = @ConstructorResult(
                targetClass = ProductResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "product_code", type = String.class),
                        @ColumnResult(name = "product_name", type = String.class),
                        @ColumnResult(name = "image_url", type = String.class),
                        @ColumnResult(name = "min_sale_price", type = BigDecimal.class),
                        @ColumnResult(name = "total_product_in_stock", type = Long.class)
                }
        )
)
public class Product extends Auditable<Long> {

    @Column(name = "product_code")
    String productCode;

    @Column(name = "product_name")
    String productName;

    @Column(name = "gender")
    int gender;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    int status;

    @ManyToOne(cascade = {ALL})
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;

    @ManyToOne(cascade = {ALL})
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    Brand brand;

    @ManyToOne(cascade = {ALL})
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    Material material;

    @OneToMany(mappedBy = "product", cascade = {ALL}, fetch = FetchType.LAZY)
    List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {ALL}, fetch = FetchType.LAZY)
    List<ProductVariant> productVariants = new ArrayList<>();

    public void addProductImage(ProductImage productImage) {
        if (productImage != null) {
            if (productVariants == null) {
                productImages = new ArrayList<>();
            }
            productImages.add(productImage);
            productImage.setProduct(this);
        }
    }

    public void addProductVariant(ProductVariant productVariant) {
        if (productVariant != null) {
            if (productVariants == null) {
                productVariants = new ArrayList<>();
            }
            productVariants.add(productVariant);
            productVariant.setProduct(this);
        }
    }

    @PostPersist
    public void prePersist() {
        if (this.productCode == null || this.productCode.isBlank()) {
            this.productCode = AppUtils.generateProductCode(this.getId());
        }
    }
}
