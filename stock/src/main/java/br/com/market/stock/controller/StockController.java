package br.com.market.stock.controller;

import br.com.market.stock.dto.ProductDTO;
import br.com.market.stock.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
