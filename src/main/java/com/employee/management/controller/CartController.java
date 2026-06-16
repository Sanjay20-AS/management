package com.employee.management.controller;

import com.employee.management.dto.CartItemRequest;
import com.employee.management.model.CartItem;
import com.employee.management.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String studentId,
                                            @RequestBody CartItemRequest request) {
        if(!cartService.addToCart(studentId, request )){
            return ResponseEntity.badRequest().body("Product is out of stock or user not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteFromCart(@RequestHeader("X-Student-ID") String studentId, @PathVariable Long productId) {
        boolean deleted=cartService.deleteItemFromCart(studentId, productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(@RequestHeader("X-Student-ID") String studentId) {
        return ResponseEntity.ok().body(cartService.getCartItems(studentId));
    }
}
