package br.com.market.stock.exceptions;

public class StockOperationException extends RuntimeException {
    public StockOperationException(String message) {
        super(message);
    }
}
