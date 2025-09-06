package br.com.market.cart.service;

import br.com.market.cart.exceptions.InsufficientStockException;
import br.com.market.cart.exceptions.ProductNotFoundException;
import br.com.market.cart.model.Cart;
import br.com.market.cart.model.CartItem;
import br.com.market.cart.model.Product;
import br.com.market.cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findProductOrThrow(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void validateStockOrThrow(Product product, int requestedQty) {
        if (product.getQuantity() < requestedQty) {
            throw new InsufficientStockException(product.getId());
        }
    }
}
