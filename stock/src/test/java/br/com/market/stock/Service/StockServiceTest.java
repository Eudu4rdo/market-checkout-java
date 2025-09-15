package br.com.market.stock.Service;

import br.com.market.stock.dto.ProductDTO;
import br.com.market.stock.model.Product;
import br.com.market.stock.repository.ProductRepository;
import br.com.market.stock.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class StockServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private StockService stockService;

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
}
