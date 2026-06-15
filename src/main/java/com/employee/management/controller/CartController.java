package com.employee.management.controller;

import com.employee.management.dto.CartItemRequest;
import com.employee.management.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
