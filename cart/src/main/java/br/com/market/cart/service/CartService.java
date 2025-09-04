package br.com.market.cart.service;

import br.com.market.cart.dto.CartDTO;
import br.com.market.cart.dto.CartItemDTO;
import br.com.market.cart.exceptions.CartNotFoundException;
import br.com.market.cart.model.Cart;
import br.com.market.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartDTO getCartById(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException(id));
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
}
