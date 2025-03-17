package com.datn.beestyle.entity.order;

import com.datn.beestyle.dto.statistics.RevenueStatisticsResponse;
import com.datn.beestyle.entity.Address;
import com.datn.beestyle.entity.Auditable;
import com.datn.beestyle.entity.Voucher;
import com.datn.beestyle.entity.product.ProductImage;
import com.datn.beestyle.entity.product.ProductVariant;
import com.datn.beestyle.entity.user.Customer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Table(name = "`order`")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SqlResultSetMapping(
        name = "RevenueStatisticsDTOMapping",
        classes = @ConstructorResult(
                targetClass = RevenueStatisticsResponse.class,
                columns = {
                        @ColumnResult(name = "date", type = java.sql.Date.class),
                        @ColumnResult(name = "revenue", type = BigDecimal.class),
                        @ColumnResult(name = "quantity", type = Long.class)
                }
        )
)
@SqlResultSetMapping(
        name = "RevenueByPeriodMapping",
        classes = @ConstructorResult(
                targetClass = RevenueStatisticsResponse.class,
                columns = {
                        @ColumnResult(name = "period", type = String.class),
                        @ColumnResult(name = "revenue", type = BigDecimal.class),
                        @ColumnResult(name = "quantity", type = Long.class)
                }
        )
)
@SqlResultSetMapping(
        name = "OrderStatusByPeriodMapping",
        classes = @ConstructorResult(
                targetClass = RevenueStatisticsResponse.class,
                columns = {
                        @ColumnResult(name = "period", type = String.class),
                        @ColumnResult(name = "total_success", type = Long.class),
                        @ColumnResult(name = "total_failed", type = Long.class)
                }
        )
)

public class Order extends Auditable<Long> {

    @Column(name = "order_tracking_number")
    String orderTrackingNumber;

    @Column(name = "receiver_name")
    String receiverName;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "shipping_fee")
    BigDecimal shippingFee = BigDecimal.ZERO;

    @Column(name = "total_amount")
    BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    Timestamp paymentDate;

    @Column(name = "payment_method")
    int paymentMethod;

    @Column(name = "is_prepaid")
    boolean isPrepaid;

    @Column(name = "order_channel")
    int orderChannel;

    @Column(name = "order_type")
    int orderType;

    @Column(name = "order_status")
    int orderStatus;

    @Column(name = "note")
    String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    Voucher voucher;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    Address shippingAddress;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        if (orderItem != null) {
            if (orderItems == null) {
                orderItems = new ArrayList<>();
            }
            orderItems.add(orderItem);
            orderItem.setOrder(this);
        }
    }
}
