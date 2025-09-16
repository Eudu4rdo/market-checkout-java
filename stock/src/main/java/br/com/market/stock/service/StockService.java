package br.com.market.stock.service;

import br.com.market.stock.dto.ProductDTO;
import br.com.market.stock.exceptions.ProductNotFoundException;
import br.com.market.stock.exceptions.StockOperationException;
import br.com.market.stock.mapper.ProductMapper;
import br.com.market.stock.model.Product;
import br.com.market.stock.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public StockService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDTO> getAllProducts() {
        return productMapper.toDTOList(productRepository.findAll());
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductDTO addProduct(Product product) {
        try {
            Product response = productRepository.save(product);
            return productMapper.toDTO(response);
        } catch (Exception e) {
            throw new StockOperationException("Add Product Fail. Message: " + e.getMessage());
        }
    }

    public ProductDTO deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
        return productMapper.toDTO(product);
    }

    public ProductDTO updateProduct(Long id, Product product) {
        Product productToUpdate = getProductById(id);
        productToUpdate.setName(product.getName());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setQuantity(product.getQuantity());
        return productMapper.toDTO(productRepository.save(productToUpdate));
    }

    public ProductDTO increaseQuantity(Long id, int quantity) {
        Product product = getProductById(id);
        product.setQuantity(product.getQuantity() + quantity);
        return productMapper.toDTO(productRepository.save(product));
    }

    public ProductDTO decreaseQuantity(Long id, int quantity) {
        Product product = getProductById(id);
        if(product.getQuantity() < quantity) {
            throw new StockOperationException("Quantity is less than the quantity to be decreased.");
        }
        product.setQuantity(product.getQuantity() - quantity);
        return productMapper.toDTO(productRepository.save(product));
    }
}
