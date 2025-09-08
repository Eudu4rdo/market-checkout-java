package br.com.market.cart.controller;

import br.com.market.cart.dto.CartDTO;
import br.com.market.cart.model.Product;
import br.com.market.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> show(@PathVariable long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addProduct(@PathVariable long id,@RequestBody Map<String, Object> productIntent) {
        Number value = (Number) productIntent.get("productId");
        Long productId = value != null ? value.longValue() : null;
        int quantity = (int) productIntent.getOrDefault("quantity", 1);
        if(productId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product ID Not Found.");
        }
        return ResponseEntity.ok(cartService.addProduct(id, productId, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id, @RequestBody Map<String, Object> productIntent) {
        Number value = (Number) productIntent.get("productId");
        Long productId = value != null ? value.longValue() : null;
        int quantity = (int) productIntent.getOrDefault("quantity", 1);
        if(productId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product ID Not Found.");
        }
        return ResponseEntity.ok(cartService.removeProduct(id, productId, quantity));
    }
}
