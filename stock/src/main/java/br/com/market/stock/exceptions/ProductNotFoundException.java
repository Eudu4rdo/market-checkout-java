package br.com.market.stock.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product Not Found: " + id);
    }
}
