package br.com.market.cart.service;

import br.com.market.cart.dto.CartDTO;
import br.com.market.cart.dto.CartItemDTO;
import br.com.market.cart.exceptions.CartNotFoundException;
import br.com.market.cart.exceptions.InsufficientStockException;
import br.com.market.cart.exceptions.ProductNotFoundException;
import br.com.market.cart.model.Cart;
import br.com.market.cart.model.CartItem;
import br.com.market.cart.model.Product;
import br.com.market.cart.repository.CartRepository;
import br.com.market.cart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Cart getCartOrThrow(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException(id));
    }

    public CartDTO getCartById(Long id) {
        Cart cart = getCartOrThrow(id);
        List<CartItemDTO> itemsDTO = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new CartDTO(cart.getId(), itemsDTO);
    }

    public void addOrUpdateCartItem(Cart cart, Product product, int quantity) {
        cart.getItems().stream()
                .filter(item -> Objects.equals(item.getProduct().getId(), product.getId()))
                .findFirst()
                .map(item -> {
                    int newQty = item.getQuantity() + quantity;
                    productService.validateStockOrThrow(product, newQty);
                    item.setQuantity(newQty);
                    return item;
                })
                .orElseGet(() -> {
                    productService.validateStockOrThrow(product, quantity);
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(quantity);
                    cart.getItems().add(newItem);
                    return newItem;
                });
    }

    @Transactional
    public CartDTO addProduct(Long cartId, long productId, int quantity) {
        Product product = productService.findProductOrThrow(productId);
        Cart cart = getCartOrThrow(cartId);
        addOrUpdateCartItem(cart, product, quantity);
        cartRepository.save(cart);
        return getCartById(cartId);
    }
}
