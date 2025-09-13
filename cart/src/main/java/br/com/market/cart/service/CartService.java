package br.com.market.cart.service;

import br.com.market.cart.dto.CartDTO;
import br.com.market.cart.dto.CartItemDTO;
import br.com.market.cart.exceptions.*;
import br.com.market.cart.model.Cart;
import br.com.market.cart.model.CartItem;
import br.com.market.cart.model.Product;
import br.com.market.cart.repository.CartRepository;
import br.com.market.cart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        List<CartItemDTO> itemsDTO = Optional.ofNullable(cart.getItems()).orElse(Collections.emptyList()).stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new CartDTO(cart.getId(), itemsDTO);
    }

    @Transactional
    public CartDTO addProduct(Long cartId, long productId, int quantity) {
        Product product = productService.findProductOrThrow(productId);
        Cart cart = getCartOrThrow(cartId);
        addOrUpdateCartItem(cart, product, quantity);
        cartRepository.save(cart);

        List<CartItemDTO> itemsDTO = Optional.ofNullable(cart.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new CartDTO(cart.getId(), itemsDTO);
    }

    @Transactional
    public CartDTO removeProduct(Long cartId, long productId, int quantity) {
        Product product = productService.findProductOrThrow(productId);
        Cart cart = getCartOrThrow(cartId);
        removeOrUpdateCartItem(cart, product, quantity);
        cartRepository.save(cart);
        return getCartById(cartId);
    }

    public void addOrUpdateCartItem(Cart cart, Product product, int quantity) {
        Optional.ofNullable(cart.getItems()).orElse(Collections.emptyList()).stream().filter(item ->
                        Objects.equals(item.getProduct().getId(), product.getId())).findFirst()
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

    public void removeOrUpdateCartItem(Cart cart, Product product, int quantity) {
        CartItem item = Optional.ofNullable(cart.getItems()).orElse(Collections.emptyList()).stream()
                .filter(i -> Objects.equals(i.getProduct().getId(), product.getId()))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(product.getId(), cart.getId()));

        if (quantity < 1) {
            throw new InvalidQuantityException(product.getQuantity(), quantity);
        }

        if (item.getQuantity() > quantity) {
            item.setQuantity(item.getQuantity() - quantity);
        } else if (item.getQuantity() == quantity) {
            cart.getItems().remove(item);
        } else {
            throw new InvalidQuantityException(item.getQuantity(), quantity);
        }
    }
}
