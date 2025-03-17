package com.datn.beestyle.service.order.item;

import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.order.item.CreateOrderItemRequest;
import com.datn.beestyle.dto.order.item.OrderItemResponse;
import com.datn.beestyle.dto.order.item.PatchUpdateQuantityOrderItem;
import com.datn.beestyle.dto.order.item.UpdateOrderItemRequest;
import com.datn.beestyle.entity.order.Order;
import com.datn.beestyle.entity.order.OrderItem;
import com.datn.beestyle.entity.product.ProductVariant;
import com.datn.beestyle.exception.InvalidDataException;
import com.datn.beestyle.mapper.OrderItemMapper;
import com.datn.beestyle.repository.OrderItemRepository;
import com.datn.beestyle.repository.ProductVariantRepository;
import com.datn.beestyle.service.order.OrderService;
import com.datn.beestyle.service.product.variant.ProductVariantService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderItemService
        extends GenericServiceAbstract<OrderItem, Long, CreateOrderItemRequest, UpdateOrderItemRequest, OrderItemResponse>
        implements IOrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;
    private final ProductVariantService productVariantService;
    private final ProductVariantRepository productVariantRepository;

    public OrderItemService(IGenericRepository<OrderItem, Long> entityRepository,
                            IGenericMapper<OrderItem, CreateOrderItemRequest, UpdateOrderItemRequest, OrderItemResponse> mapper,
                            EntityManager entityManager, OrderItemRepository orderItemRepository, OrderService orderService,
                            ProductVariantService productVariantService, ProductVariantRepository productVariantRepository,
                            OrderItemMapper orderItemMapper) {
        super(entityRepository, mapper, entityManager);
        this.orderItemRepository = orderItemRepository;
        this.orderService = orderService;
        this.productVariantService = productVariantService;
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public List<OrderItemResponse> getAllByOrderId(Long orderId) {
        return orderItemRepository.findOrderItemsResponseByOrderId(orderId);
    }

    /**
     * Bán tại quầy, xử lý cộng và trừ sản phẩm luôn trong kho
     * @param orderId
     * @param requests
     * @return
     */
    @Override
    public Map<Long, Long> createOrUpdateOrderItems(Long orderId, List<UpdateOrderItemRequest> requests) {
        Map<Long, Long> newOrderItemsMap = new HashMap<>();
        List<Long> orderItemIds;
        List<Long> productVariantIds;
        List<OrderItem> orderItemsToSave = new ArrayList<>();

        // kiểm hóa đơn có hợp lệ hay tồn tại không
        if (orderId == null) throw new InvalidDataException("Id hóa đơn không hợp lệ (null).");
        Order order = orderService.getById(orderId);

        if (requests.isEmpty()) throw new InvalidDataException("Vui lòng chọn sản phẩm.");
        // lấy ra orderItemIds và productVariantIds
        orderItemIds = new ArrayList<>();
        productVariantIds = new ArrayList<>();
        for (UpdateOrderItemRequest request : requests) {
            if (request.getId() != null) orderItemIds.add(request.getId());
            if (request.getProductVariantId() != null) productVariantIds.add(request.getProductVariantId());
        }

        // validate tồn tại OrderItem
        Map<Long, OrderItem> orderItemMap = Collections.emptyMap();
        if (!orderItemIds.isEmpty()) {
            List<OrderItem> orderItemList = orderItemRepository.findAllById(orderItemIds);

            orderItemMap = orderItemList.stream().collect(Collectors.toMap(OrderItem::getId, orderItem -> orderItem));

            this.validatePropOrderItems(orderId, orderItemIds, orderItemMap);
        }

        // validate tồn tại ProductVariant
        Map<Long, ProductVariant> productVariantMap = Collections.emptyMap();
        if (!productVariantIds.isEmpty()) {
            List<ProductVariant> productVariantList = productVariantRepository.findAllById(productVariantIds);

            productVariantMap = productVariantList.stream()
                    .collect(Collectors.toMap(ProductVariant::getId, productVariant -> productVariant));

            this.validatePropProductVariants(productVariantIds, productVariantMap);
        }

        for (UpdateOrderItemRequest request : requests) {
            ProductVariant productVariant = productVariantMap.get(request.getProductVariantId());

            OrderItem orderItem;
            if (request.getId() != null) {
                // Cập nhật OrderItem đã tồn tại
                orderItem = orderItemMap.get(request.getId());
                orderItem.setOrder(order);
                orderItem.setSalePrice(productVariant.getSalePrice());

                int newQuantity = orderItem.getQuantity() + request.getQuantity();
                orderItem.setQuantity(newQuantity);
            } else {
                // Tạo mới OrderItem
                orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductVariant(productVariant);
                orderItem.setQuantity(request.getQuantity());
                orderItem.setSalePrice(productVariant.getSalePrice());
            }

            // Cập nhật số lượng tồn kho
            int newStockQuantity = productVariant.getQuantityInStock() - request.getQuantity();
            // Kiểm tra số lượng tồn kho có đủ hay không
            if (newStockQuantity < 0) {
                throw new InvalidDataException("Số lượng tồn kho không đủ cho sản phẩm: " + productVariant.getSku());
            }
            productVariant.setQuantityInStock(newStockQuantity);

            // Thêm vào danh sách để lưu
            orderItemsToSave.add(orderItem);
        }
        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItemsToSave);
        productVariantRepository.saveAll(productVariantMap.values());

        for (OrderItem newOrderItem : savedOrderItems) {
            newOrderItemsMap.put(newOrderItem.getProductVariant().getId(), newOrderItem.getId());
        }

        return newOrderItemsMap;
    }

    /**
     * Bán giao hàng, thay đổi sản phẩm trong giỏ khi trng thái hóa đơn chờ thanh toán
     * @param orderId
     * @param requests
     * @return
     */
    @Override
    public Map<Long, Long> createOrUpdateOrderItemsDeliverySale(Long orderId, List<UpdateOrderItemRequest> requests) {
        Map<Long, Long> newOrderItemsMap = new HashMap<>();
        List<Long> orderItemIds;
        List<Long> productVariantIds;
        List<OrderItem> orderItemsToSave = new ArrayList<>();

        // kiểm hóa đơn có hợp lệ hay tồn tại không
        if (orderId == null) throw new InvalidDataException("Id hóa đơn không hợp lệ (null).");
        Order order = orderService.getById(orderId);

        if (requests.isEmpty()) throw new InvalidDataException("Vui lòng chọn sản phẩm.");
        // lấy ra orderItemIds và productVariantIds
        orderItemIds = new ArrayList<>();
        productVariantIds = new ArrayList<>();
        for (UpdateOrderItemRequest request : requests) {
            if (request.getId() != null) orderItemIds.add(request.getId());
            if (request.getProductVariantId() != null) productVariantIds.add(request.getProductVariantId());
        }

        // validate tồn tại OrderItem
        Map<Long, OrderItem> orderItemMap = Collections.emptyMap();
        if (!orderItemIds.isEmpty()) {
            List<OrderItem> orderItemList = orderItemRepository.findAllById(orderItemIds);

            orderItemMap = orderItemList.stream().collect(Collectors.toMap(OrderItem::getId, orderItem -> orderItem));

            this.validatePropOrderItems(orderId, orderItemIds, orderItemMap);
        }

        // validate tồn tại ProductVariant
        Map<Long, ProductVariant> productVariantMap = Collections.emptyMap();
        if (!productVariantIds.isEmpty()) {
            List<ProductVariant> productVariantList = productVariantRepository.findAllById(productVariantIds);

            productVariantMap = productVariantList.stream()
                    .collect(Collectors.toMap(ProductVariant::getId, productVariant -> productVariant));

            this.validatePropProductVariants(productVariantIds, productVariantMap);
        }

        for (UpdateOrderItemRequest request : requests) {
            ProductVariant productVariant = productVariantMap.get(request.getProductVariantId());

            // Kiểm tra số lượng tồn kho có đủ hay không
            int newStockQuantity = productVariant.getQuantityInStock() - request.getQuantity();
            if (newStockQuantity < 0) {
                throw new InvalidDataException("Số lượng tồn kho không đủ cho sản phẩm: " + productVariant.getSku());
            }

            OrderItem orderItem;
            if (request.getId() != null) {
                // Cập nhật OrderItem đã tồn tại
                orderItem = orderItemMap.get(request.getId());
                orderItem.setOrder(order);
                orderItem.setSalePrice(productVariant.getSalePrice());

                int newQuantity = orderItem.getQuantity() + request.getQuantity();
                orderItem.setQuantity(newQuantity);
            } else {
                // Tạo mới OrderItem
                orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductVariant(productVariant);
                orderItem.setQuantity(request.getQuantity());
                orderItem.setSalePrice(productVariant.getSalePrice());
            }

            // Thêm vào danh sách để lưu
            orderItemsToSave.add(orderItem);
        }
        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItemsToSave);

        for (OrderItem newOrderItem : savedOrderItems) {
            newOrderItemsMap.put(newOrderItem.getProductVariant().getId(), newOrderItem.getId());
        }

        return newOrderItemsMap;
    }

    @Transactional
    @Override
    public int patchUpdateQuantity(PatchUpdateQuantityOrderItem request) {
        if (request.getId() == null) throw new InvalidDataException("Id hóa đơn chi tiết không hợp lệ.");
        this.getById(request.getId());

        return orderItemRepository.updateQuantityOrderItem(request.getId(), request.getQuantity());
    }

    /**
     * xóa sản phẩm trong giỏ khi bán hag tại quầy
     * hồi lại sản phẩm ngay vào kho
     * @param id
     */
    @Transactional
    @Override
    public void delete(Long id) {
        if (id == null) throw new InvalidDataException("Id Hóa đơn chi tiết không tồn tại với ID: " + id);
        OrderItem orderItem = this.getById(id);

        Long productVariantId = orderItem.getProductVariant().getId();
        ProductVariant productVariant = productVariantService.getById(productVariantId);

        // tính và hồi lại số lượng sản phẩm vào kho
        int quantity = orderItem.getQuantity() + productVariant.getQuantityInStock();
        productVariantRepository.updateQuantityProductVariant(productVariantId, quantity);

        super.delete(id);
    }

    @Override
    protected List<CreateOrderItemRequest> beforeCreateEntities(List<CreateOrderItemRequest> requests) {
        return null;
    }

    @Override
    protected List<UpdateOrderItemRequest> beforeUpdateEntities(List<UpdateOrderItemRequest> requests) {
        return null;
    }

    @Override
    protected void beforeCreate(CreateOrderItemRequest request) {

    }

    @Override
    protected void beforeUpdate(Long orderId, UpdateOrderItemRequest request) {
        if (orderId == null) throw new InvalidDataException("Id hóa đơn không hợp lệ (null).");
    }

    @Override
    protected void afterConvertCreateRequest(CreateOrderItemRequest request, OrderItem entity) {
        Long orderId = request.getOrderId();
        Order order = orderService.getById(orderId);
        entity.setOrder(order);

        Long productVariantId = request.getProductVariantId();
        ProductVariant productVariant = productVariantService.getById(productVariantId);
        entity.setProductVariant(productVariant);
    }

    @Override
    protected void afterConvertUpdateRequest(UpdateOrderItemRequest request, OrderItem entity) {
        Long orderId = request.getOrderId();
        Order order = orderService.getById(orderId);
        entity.setOrder(order);
    }

    @Override
    protected String getEntityName() {
        return "Order Item";
    }

    private void validatePropProductVariants(List<Long> productVariantIds, Map<Long, ProductVariant> productVariantMap) {
        List<Long> invalidProductVariantIds = productVariantIds.stream()
                .filter(id -> productVariantMap.get(id) == null)
                .toList();

        if (!invalidProductVariantIds.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("ID biến thể sản phẩm không tồn tại: ");
            for (Long invalidId : invalidProductVariantIds) {
                errorMessage.append(invalidId).append(", ");
            }
            errorMessage.setLength(errorMessage.length() - 2);
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }

    private void validatePropOrderItems(Long orderId, List<Long> orderItemIds, Map<Long, OrderItem> orderItemMap) {
        List<Long> invalidOrderItemIds = orderItemIds.stream()
                .filter(id -> orderItemMap.get(id) == null ||
                              !Objects.equals(orderItemMap.get(id).getOrder().getId(), orderId))
                .toList();

        if (!invalidOrderItemIds.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Hóa đơn chi tiết ID không tồn tại: ");
            for (Long invalidId : invalidOrderItemIds) {
                errorMessage.append(invalidId).append(", ");
            }
            errorMessage.setLength(errorMessage.length() - 2);
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }
}
