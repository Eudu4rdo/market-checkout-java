package br.com.market.cart.dto;

public class CartItemDTO {
    private Long productId;
    private String productName;
    private Double price;
    private int quantity;
    private Double subtotal;

    public CartItemDTO(Long productId, String productName, Double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = price * quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }
}
