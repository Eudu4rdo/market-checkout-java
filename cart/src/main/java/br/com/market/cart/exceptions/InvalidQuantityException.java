package br.com.market.cart.exceptions;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(int productQty, int requestedQty) {
        super("Requested Product Quantity Invalid: " + requestedQty + " of " + productQty);
    }
}
