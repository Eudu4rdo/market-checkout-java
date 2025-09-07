package br.com.market.cart.exceptions;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(long product_id, long cart_id) {
        super("Item "+ product_id +" not found in cart " + cart_id);
    }
}