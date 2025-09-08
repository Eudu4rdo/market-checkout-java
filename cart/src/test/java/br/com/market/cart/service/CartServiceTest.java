package br.com.market.cart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import br.com.market.cart.dto.CartDTO;
import br.com.market.cart.dto.CartItemDTO;
import br.com.market.cart.model.Cart;
import br.com.market.cart.model.Product;
import br.com.market.cart.repository.CartRepository;
import br.com.market.cart.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductService productService;
    @InjectMocks
    private CartService cartService;

    private Cart createCart(Long id) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setItems(new ArrayList<>());
        return cart;
    }

    private Product createProduct(Long id, String name, Double price, int stock) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(stock);
        return product;
    }

    @Test
    void shouldReturnCartDTOWhenCartExists() {
        Cart cart = createCart(1L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        CartDTO cartDTO = cartService.getCartById(1L);

        assertNotNull(cartDTO);
        assertEquals(1L, cartDTO.getCartId());
        verify(cartRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCartDoesNotExist() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.getCartById(1L));

        assertEquals("Cart Not Found With ID: 1", exception.getMessage());
        verify(cartRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyCartDTOWhenCartHasNoItems() {
        Cart cart = createCart(1L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        CartDTO cartDTO = cartService.getCartById(1L);

        assertNotNull(cartDTO);
        assertEquals(1L, cartDTO.getCartId());
        assertTrue(cartDTO.getItems().isEmpty());
        assertEquals(0.0, cartDTO.getTotal());
        verify(cartRepository).findById(1L);
    }

    @Test
    void shouldAddProductToCartSuccessfully() {
        Cart cart = createCart(1L);
        Product product = createProduct(1L, "Product 1", 10.0, 10);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        doReturn(product).when(productService).findProductOrThrow(1L);

        CartDTO cartDTO = cartService.addProduct(1L, 1L, 2);

        assertNotNull(cartDTO);
        assertEquals(1L, cartDTO.getCartId());
        assertEquals(1, cartDTO.getItems().size());

        CartItemDTO itemDTO = cartDTO.getItems().get(0);
        assertEquals(1L, itemDTO.getProductId());
        assertEquals("Product 1", itemDTO.getProductName());
        assertEquals(10.0, itemDTO.getPrice());
        assertEquals(2, itemDTO.getQuantity());
        assertEquals(20.0, cartDTO.getTotal());

        verify(cartRepository).findById(1L);
        verify(productService).findProductOrThrow(1L);
    }
}
