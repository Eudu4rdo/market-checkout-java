package br.com.market.cart.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long id) {
        super("Cart Not Found With ID: " + id);
    }
}
