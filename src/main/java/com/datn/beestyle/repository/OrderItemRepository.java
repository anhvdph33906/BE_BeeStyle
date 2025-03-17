package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.order.item.OrderItemResponse;
import com.datn.beestyle.entity.order.OrderItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderItemRepository extends IGenericRepository<OrderItem, Long> {

    @Query("""
        select new com.datn.beestyle.dto.order.item.OrderItemResponse(
            oi.id, oi.order.id, pv.id, pv.sku, p.id, p.productName,c.id, c.colorCode, c.colorName, s.id, s.sizeName,
            oi.quantity, oi.salePrice, oi.discountedPrice, oi.note
        )
        from OrderItem oi
            left join ProductVariant pv on oi.productVariant.id = pv.id
            join Product p on pv.product.id = p.id
            left join Color c on pv.color.id = c.id
            left join Size s on pv.size.id = s.id
        where oi.order.id = :orderId
    """)
    List<OrderItemResponse> findOrderItemsResponseByOrderId(@Param("orderId") Long orderId);


    @Transactional
    @Modifying
    @Query(value = "update OrderItem oi set oi.quantity = :quantity where oi.id = :orderItemId")
    int updateQuantityOrderItem(@Param("orderItemId") long orderItemId, @Param("quantity") int quantity);

    List<OrderItem> findOrderItemsByOrderId(Long orderId);
}
