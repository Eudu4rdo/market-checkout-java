package br.com.market.stock.Service;

import br.com.market.stock.dto.ProductDTO;
import br.com.market.stock.mapper.ProductMapper;
import br.com.market.stock.model.Product;
import br.com.market.stock.repository.ProductRepository;
import br.com.market.stock.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class StockServiceTest {
    @Mock
    private ProductRepository productRepository;
    private StockService stockService;
    @BeforeEach
    void setUp() {
        stockService = new StockService(productRepository, new ProductMapper());
    }

    private Product createProduct(Long id, String name, Double price, int quantity) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        return product;
    }

    @Test
    void shoudReturnEmptyStock() {
        List<Product> stock = productRepository.findAll();
        assertTrue(stock.isEmpty());
    }

    @Test
    void shoudReturProducsInStock() {
        Product product = createProduct(1L, "Product 1", 10.0, 10);
        List<Product> products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);
        List<ProductDTO> result = stockService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals(10.0, result.get(0).getPrice());
        assertEquals(10, result.get(0).getQuantity());
    }

    @Test
    void shoudIncludeProductInStock() {
        Product product = createProduct(1L, "Product 1", 10.0, 10);
        ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getQuantity());

        when(productRepository.save(product)).thenReturn(product);
        ProductDTO result = stockService.addProduct(product);

        assertNotNull(result);
        assertEquals("Product 1", result.getName());
        assertEquals(10.0, result.getPrice());
        assertEquals(10, result.getQuantity());
    }

    @Test
    void shoudNotIncludeProductInStock() {
        Product product = createProduct(1L, "Product 1", 10.0, 10);
        when(productRepository.save(product)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            stockService.addProduct(product);
        });
    }

    @Test
    void shoudDeleteProductInStock() {
        Product product = createProduct(1L, "Product 1", 10.0, 10);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        ProductDTO productDTO = stockService.deleteProduct(product.getId());
        assertNotNull(productDTO);
        assertEquals("Product 1", productDTO.getName());
        assertEquals(10.0, productDTO.getPrice());
    }

    @Test
    void shoudExceptionWhenDeleteProductInStock() {
        Product product = createProduct(1L, "Product 1", 10.0, 10);
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            stockService.deleteProduct(product.getId());
        });
    }

    @Test
    void shoudUpdateProductInStock() {
        Product existing = createProduct(1L, "Product 1", 10.0, 10);
        Product updating = createProduct(1L, "Product 2", 20.0, 20);
        when(productRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(updating);

        ProductDTO result = stockService.updateProduct(existing.getId(), updating);
        assertEquals("Product 2", result.getName());
        assertEquals(20.0, result.getPrice());
        assertEquals(20, result.getQuantity());

        verify(productRepository, times(1)).findById(existing.getId());
        verify(productRepository, times(1)).save(existing);
    }

    @Test
    void shoudIncreaseQuantityInProductStock() {
        Product original = createProduct(1L, "Product 1", 10.0, 10);
        Product updated  = createProduct(1L, "Product 1", 10.0, 15);
        when(productRepository.findById(original.getId())).thenReturn(Optional.of(original));
        when(productRepository.save(any(Product.class))).thenReturn(updated);
        ProductDTO result = stockService.increaseQuantity(original.getId(), 5);

        assertEquals("Product 1", result.getName());
        assertEquals(10.0, result.getPrice());
        assertEquals(15, result.getQuantity());

        verify(productRepository, times(1)).findById(original.getId());
        verify(productRepository, times(1)).save(original);
    }

    @Test
    void shoudDecreaseQuantityInProductStock() {
        Product original = createProduct(1L, "Product 1", 10.0, 10);
        Product updated  = createProduct(1L, "Product 1", 10.0, 5);
        when(productRepository.findById(original.getId())).thenReturn(Optional.of(original));
        when(productRepository.save(any(Product.class))).thenReturn(updated);
        ProductDTO result = stockService.decreaseQuantity(original.getId(), 5);

        assertEquals("Product 1", result.getName());
        assertEquals(10.0, result.getPrice());
        assertEquals(5, result.getQuantity());

        verify(productRepository, times(1)).findById(original.getId());
        verify(productRepository, times(1)).save(original);
    }

    @Test
    void shoudExceptionWhenDecreaseQuantityInProductStock() {
        Product product = createProduct(1L, "Product 1", 10.0, 10);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        assertThrows(RuntimeException.class, () -> {
            stockService.decreaseQuantity(product.getId(), 20);
        });
    }
}
