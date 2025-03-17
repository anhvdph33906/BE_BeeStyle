package com.datn.beestyle.controller.user;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.cart.ShoppingCartRequest;
import com.datn.beestyle.dto.cart.ShoppingCartResponse;
import com.datn.beestyle.dto.product.variant.ProductVariantResponse;
import com.datn.beestyle.service.shoppingcart.IShoppingCartService;
import com.datn.beestyle.service.user.UserProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final UserProductVariantService productVariantService;
    private final IShoppingCartService shoppingCartService;

    @PostMapping
    public ApiResponse<?> getCartByCustomer(@RequestBody Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("CUSTOMER_ID IS NULL");
        }
        List<ShoppingCartResponse> cartItems = this.shoppingCartService
                .findShoppingCartByCustomerId(customerId);
        return new ApiResponse<>(HttpStatus.OK.value(), "Get cart items success", cartItems);
    }

    @PostMapping("/check")
    public ResponseEntity<List<ProductVariantResponse>> checkCart(
            @RequestBody List<ShoppingCartRequest> cartItemsRequest
    ) {
        List<ProductVariantResponse> productVariantResponses = this.productVariantService
                .getProductVariantByIds(cartItemsRequest);
        return ResponseEntity.ok(Objects.requireNonNullElseGet(productVariantResponses, ArrayList::new));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCartItems(
            @RequestBody @Valid List<ShoppingCartRequest> cartRequests
    ) {
        this.shoppingCartService.createCartItemOnline(cartRequests);
        return ResponseEntity.ok().body("Cart item update success");
    }

    @PostMapping("/update/quantity")
    public void updateQuantityCart(@RequestBody ShoppingCartRequest requestData) {
        Long cartId = requestData.getId();
        Integer quantity = requestData.getQuantity() > 0 ? requestData.getQuantity() : 1;

        this.shoppingCartService.updateCartQuantityById(cartId, quantity);
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        this.shoppingCartService.delete(id);
        return ResponseEntity.ok().body("Delele cart item success");
    }

    @GetMapping("/deleteAll")
    public ResponseEntity<?> deleteAllCart() {
        this.shoppingCartService.deleteAllCartItems();
        return ResponseEntity.ok().body("Delele all cart items success");
    }
}

