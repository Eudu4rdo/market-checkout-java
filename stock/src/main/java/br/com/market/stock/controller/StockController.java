package br.com.market.stock.controller;

import br.com.market.stock.dto.ProductDTO;
import br.com.market.stock.dto.QuantityDTO;
import br.com.market.stock.model.Product;
import br.com.market.stock.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> show(){
        return ResponseEntity.ok(stockService.getAllProducts());
    }

    @PostMapping("/product/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product){
        return ResponseEntity.ok(stockService.addProduct(product));
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(stockService.deleteProduct(id));
    }

    @PostMapping("/product/{id}/edit")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product product){
        System.out.println(product);
        return ResponseEntity.ok(stockService.updateProduct(id, product));
    }

    @PostMapping("/product/{id}/increase")
    public ResponseEntity<ProductDTO> increaseQuantity(@PathVariable Long id, @RequestBody QuantityDTO dto){
        return ResponseEntity.ok(stockService.increaseQuantity(id, dto.getQuantity()));
    }

    @PostMapping("/product/{id}/decrease")
    public ResponseEntity<ProductDTO> decreaseQuantity(@PathVariable Long id, @RequestBody QuantityDTO dto){
        return ResponseEntity.ok(stockService.decreaseQuantity(id, dto.getQuantity()));
    }
}
