package com.datn.beestyle.entity;

import com.datn.beestyle.entity.product.ProductVariant;
import com.datn.beestyle.enums.DiscountType;
import com.datn.beestyle.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;

@Table(name = "promotion")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Promotion extends Auditable<Integer> {

    @Column(name = "promotion_name")
    String promotionName;

    @Column(name = "discount_type")
    int discountType;

    @Column(name = "discount_value")
    int discountValue;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    Timestamp startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    Timestamp endDate;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    int status;

    @OneToMany(mappedBy = "promotion", cascade = {PERSIST, MERGE}, fetch = FetchType.LAZY)
    List<ProductVariant> productVariants = new ArrayList<>();

//    public Status getStatus() {
//        return Status.resolve(this.status);
//    }
//
//    public void setStatus(Status status) {
//        this.status = (status != null) ? status.getValue() : Status.INACTIVE.getValue();
//    }
}
