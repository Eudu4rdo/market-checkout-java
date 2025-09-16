package br.com.market.stock.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AddProductException extends RuntimeException {
    public AddProductException() {
        super("Unable to add product to stock");
    }
}
