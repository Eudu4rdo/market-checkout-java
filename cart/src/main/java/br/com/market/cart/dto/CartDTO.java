package br.com.market.cart.dto;

import java.util.List;

public class CartDTO {
    private Long cartId;
    private List<CartItemDTO> items;
    private Double total;

    public CartDTO(Long cartId, List<CartItemDTO> items) {
        this.cartId = cartId;
        this.items = items;
        this.total = items.stream()
                .mapToDouble(CartItemDTO::getSubtotal)
                .sum();
    }

    public Long getCartId() {
        return cartId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public Double getTotal() {
        return total;
    }
}
