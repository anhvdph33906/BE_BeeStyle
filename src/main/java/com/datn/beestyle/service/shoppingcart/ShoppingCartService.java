package com.datn.beestyle.service.shoppingcart;

import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.cart.ShoppingCartRequest;
import com.datn.beestyle.dto.cart.ShoppingCartResponse;
import com.datn.beestyle.entity.ShoppingCart;
import com.datn.beestyle.entity.product.ProductVariant;
import com.datn.beestyle.entity.user.Customer;
import com.datn.beestyle.repository.ProductVariantRepository;
import com.datn.beestyle.repository.ShoppingCartRepository;
import com.datn.beestyle.repository.customer.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShoppingCartService implements IShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CustomerRepository customerRepository;

    public ShoppingCartService(ShoppingCartRepository cartRepository, ProductVariantRepository productVariantRepository, CustomerRepository customerRepository) {
        this.cartRepository = cartRepository;
        this.productVariantRepository = productVariantRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<ShoppingCartResponse> findShoppingCartByCustomerId(Long customerId) {
        return this.cartRepository.findShoppingCartByCustomerId(customerId);
    }

    @Modifying
    @Transactional
    public void createCartItemOnline(List<ShoppingCartRequest> cartRequests) {

        if (cartRequests == null || cartRequests.isEmpty()) {
            return;
        }

        // Nhóm các request theo customerId
        Map<Long, List<ShoppingCartRequest>> requestsByCustomerId = cartRequests.stream()
                .collect(Collectors.groupingBy(ShoppingCartRequest::getCustomerId));

        for (Map.Entry<Long, List<ShoppingCartRequest>> entry : requestsByCustomerId.entrySet()) {
            Long customerId = entry.getKey(); // Lấy customerId từ key của Map.Entry
            List<ShoppingCartRequest> customerCartRequests = entry.getValue();

            List<Long> productVariantIds = customerCartRequests.stream()
                    .map(ShoppingCartRequest::getProductVariantId)
                    .collect(Collectors.toList());

            // Lấy danh sách ShoppingCartResponse dựa trên productVariantIds và customerId
            List<ShoppingCartResponse> existingCartResponses = this.cartRepository
                    .findByProductVariantIdInAndCustomerId(productVariantIds, customerId);

            // Chuyển đổi List<ShoppingCartResponse> thành Map<Long, ShoppingCartResponse>
            Map<Long, ShoppingCartResponse> existingCartResponseMap = existingCartResponses.stream()
                    .collect(Collectors.toMap(ShoppingCartResponse::getProductVariantId, Function.identity()));

            List<ShoppingCart> cartsToSave = new ArrayList<>();
            for (ShoppingCartRequest request : customerCartRequests) {
                Long productVariantId = request.getProductVariantId();
                ShoppingCartResponse existingCartResponse = existingCartResponseMap.get(productVariantId);
                ProductVariant productVariant = productVariantRepository.findById(productVariantId).orElse(null);

                if (existingCartResponse != null) {
                    // Cập nhật quantity nếu cart đã tồn tại
                    ShoppingCart existingCart = cartRepository.findById(existingCartResponse.getId()).orElse(null);
                    if (existingCart != null &&
                            productVariant != null &&
                            productVariant.getQuantityInStock() >= request.getQuantity()
                    ) {
                        existingCart.setQuantity(existingCart.getQuantity() + request.getQuantity());
                        cartsToSave.add(existingCart);
                    } else {
                        throw new NullPointerException("Thông tin đơn hàng không chính xác");
                    }
                } else {
                    // Tạo mới cart nếu chưa tồn tại
                    ShoppingCart newCart = new ShoppingCart();
                    Customer customer = customerRepository.findById(customerId).orElse(null); // Sử dụng customerId từ Map.Entry

                    if (productVariant != null && customer != null && productVariant.getQuantityInStock() >= request.getQuantity()) {
                        newCart.setProductVariant(productVariant);
                        newCart.setCustomer(customer);
                        newCart.setQuantity(request.getQuantity());
                        newCart.setCartCode(request.getCartCode());
                        newCart.setSalePrice(productVariant.getSalePrice());
                        cartsToSave.add(newCart);
                    } else {
                        throw new NullPointerException("Thông tin đơn hàng không chính xác");
                    }
                }
            }

            cartRepository.saveAll(cartsToSave);
        }
    }

    @Override
    public void updateCartQuantityById(Long id, Integer quantity) {
        ShoppingCart cart = this.cartRepository.findById(id).get();
        Long productVariantId = cart.getProductVariant().getId();
        ProductVariant productVariant = this.productVariantRepository.findById(productVariantId).get();
        int quantityInStock = productVariant.getQuantityInStock();

        if (quantityInStock >= quantity) {
            cart.setQuantity(quantity);
            this.cartRepository.save(cart);
        }
    }

    @Override
    public void deleteAllCartItems() {
        this.cartRepository.deleteAll();
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            ShoppingCart cart = this.cartRepository.findById(id).get();
            this.cartRepository.delete(cart);
        }
    }

    @Override
    public PageResponse<?> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public ShoppingCartResponse create(ShoppingCartRequest request) {
        return null;
    }

    @Override
    public ShoppingCartResponse update(Long aLong, ShoppingCartRequest request) {
        return null;
    }

    @Override
    public ShoppingCartResponse getDtoById(Long aLong) {
        return null;
    }

    @Override
    public ShoppingCart getById(Long aLong) {
        return null;
    }

    @Override
    public List<ShoppingCartResponse> createEntities(List<ShoppingCartRequest> requests) {
        return null;
    }

    @Override
    public void updateEntities(List<ShoppingCartRequest> requests) {

    }

    @Override
    public List<ShoppingCartResponse> getAllById(Set<Long> longs) {
        return null;
    }
}
